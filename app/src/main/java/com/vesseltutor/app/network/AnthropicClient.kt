package com.vesseltutor.app.network

import android.util.Base64
import com.vesseltutor.app.data.ApiKeyStore
import com.vesseltutor.app.data.local.entity.ScenarioEntity
import com.vesseltutor.app.data.seed.Difficulty
import com.vesseltutor.app.data.seed.ScenarioCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AnthropicException(message: String) : Exception(message)

/**
 * Thin client for Anthropic's Messages API, used to turn a typed topic, a photo, or a pasted
 * document into a new practice scenario. Uses the caller's own API key (see [ApiKeyStore]) —
 * this app never ships or proxies a shared key.
 */
class AnthropicClient(private val apiKeyStore: ApiKeyStore) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateFromTopic(topic: String): ScenarioEntity =
        generate("Create a speaking-practice scenario about this topic: $topic")

    suspend fun generateFreshTopic(): ScenarioEntity =
        generate(
            "Suggest a fresh, realistic, specific scenario related to shipping and maritime " +
                "work, or everyday professional life, that would be useful to practice speaking " +
                "about today. Make it feel current and specific, not generic."
        )

    suspend fun generateFromDocumentText(documentText: String): ScenarioEntity =
        generate("Here is a document I want to practice discussing out loud:\n\n$documentText")

    suspend fun generateFromImage(imageBytes: ByteArray, mimeType: String): ScenarioEntity =
        generate(
            userText = "This image may be a work document, email, notice, or everyday photo. " +
                "Look at it and create a speaking-practice scenario based on what it shows.",
            imageBase64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP),
            imageMimeType = mimeType
        )

    private suspend fun generate(
        userText: String,
        imageBase64: String? = null,
        imageMimeType: String? = null
    ): ScenarioEntity = withContext(Dispatchers.IO) {
        val apiKey = apiKeyStore.apiKey.value
        if (apiKey.isBlank()) throw AnthropicException("No API key set. Add one in Settings first.")

        val contentArray = JSONArray()
        if (imageBase64 != null && imageMimeType != null) {
            contentArray.put(
                JSONObject().apply {
                    put("type", "image")
                    put(
                        "source",
                        JSONObject().apply {
                            put("type", "base64")
                            put("media_type", imageMimeType)
                            put("data", imageBase64)
                        }
                    )
                }
            )
        }
        contentArray.put(JSONObject().apply {
            put("type", "text")
            put("text", userText)
        })

        val messages = JSONArray().put(
            JSONObject().apply {
                put("role", "user")
                put("content", contentArray)
            }
        )

        val requestJson = JSONObject().apply {
            put("model", MODEL)
            put("max_tokens", 1024)
            put("system", SYSTEM_PROMPT)
            put("messages", messages)
        }

        val request = Request.Builder()
            .url("https://api.anthropic.com/v1/messages")
            .addHeader("x-api-key", apiKey)
            .addHeader("anthropic-version", "2023-06-01")
            .addHeader("content-type", "application/json")
            .post(requestJson.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw AnthropicException(errorMessageFor(response.code, responseBody))
            }
            parseScenario(responseBody)
        }
    }

    private fun errorMessageFor(code: Int, body: String): String = when (code) {
        401 -> "That API key was rejected. Double-check it in Settings."
        429 -> "Rate limited by Anthropic — wait a moment and try again."
        in 500..599 -> "Anthropic's service had an error, please try again shortly."
        else -> {
            val detail = runCatching {
                JSONObject(body).optJSONObject("error")?.optString("message")
            }.getOrNull()
            detail?.takeIf { it.isNotBlank() } ?: "Request failed (HTTP $code)."
        }
    }

    private fun parseScenario(responseBody: String): ScenarioEntity {
        val root = JSONObject(responseBody)
        val contentArray = root.optJSONArray("content")
            ?: throw AnthropicException("Empty response from Claude.")

        val text = (0 until contentArray.length())
            .map { contentArray.getJSONObject(it) }
            .firstOrNull { it.optString("type") == "text" }
            ?.optString("text")
            ?: throw AnthropicException("Claude didn't return any text.")

        val json = extractJsonObject(text)
            ?: throw AnthropicException("Couldn't understand Claude's response. Please try again.")

        val category = json.optString("category", ScenarioCategory.MY_TOPICS)
            .takeIf { it in ScenarioCategory.ALL } ?: ScenarioCategory.MY_TOPICS
        val difficulty = json.optString("difficulty", Difficulty.BEGINNER)
            .takeIf { it == Difficulty.BEGINNER || it == Difficulty.INTERMEDIATE || it == Difficulty.ADVANCED }
            ?: Difficulty.BEGINNER

        return ScenarioEntity(
            category = category,
            difficulty = difficulty,
            context = json.optString("context"),
            promptText = json.optString("prompt"),
            keyPhrases = json.optJSONArray("keyPhrases").toStringList().joinToString("|"),
            vocabulary = json.optJSONArray("vocabulary").toStringList().joinToString("|"),
            sampleAnswer = json.optString("sampleAnswer"),
            isUserGenerated = true
        )
    }

    private fun extractJsonObject(text: String): JSONObject? {
        val start = text.indexOf('{')
        val end = text.lastIndexOf('}')
        if (start == -1 || end == -1 || end < start) return null
        return runCatching { JSONObject(text.substring(start, end + 1)) }.getOrNull()
    }

    private fun JSONArray?.toStringList(): List<String> {
        if (this == null) return emptyList()
        return (0 until length()).map { optString(it) }.filter { it.isNotBlank() }
    }

    private companion object {
        const val MODEL = "claude-haiku-4-5-20251001"
        val SYSTEM_PROMPT = """
            You create English speaking-practice scenarios for a beginner-to-intermediate,
            non-native English speaker who works as a Vessel Manager in shipping, and also
            wants everyday life practice. Given the user's request, respond with ONLY a single
            JSON object, no markdown, no commentary, with exactly these fields:
            {
              "category": one of "Status Updates","Phone Calls","Meetings","Explaining Problems","Requests","Confirmations","Daily Life","My Topics" (pick the closest fit, or "My Topics" if none fit well),
              "difficulty": one of "Beginner","Intermediate","Advanced",
              "context": one short sentence setting the scene, starting with "You",
              "prompt": a short speaking prompt or question, under 20 words,
              "keyPhrases": array of 3-4 short useful phrases a strong spoken answer would include,
              "vocabulary": array of 3 relevant vocabulary words or terms,
              "sampleAnswer": one natural example answer, 1-3 sentences, simple enough for a beginner to read aloud and repeat
            }
            Use simple, clear language suitable for a beginner English learner. Output valid JSON only.
        """.trimIndent()
    }
}
