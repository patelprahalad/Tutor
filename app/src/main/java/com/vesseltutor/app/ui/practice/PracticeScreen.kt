package com.vesseltutor.app.ui.practice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.vesseltutor.app.feedback.FeedbackResult
import com.vesseltutor.app.ui.components.MicButton
import com.vesseltutor.app.ui.theme.AlertRed
import com.vesseltutor.app.ui.theme.SuccessGreen
import com.vesseltutor.app.ui.theme.WarningAmber

@Composable
fun PracticeScreen(
    viewModel: PracticeViewModel,
    initialScenarioId: Long?
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(initialScenarioId) {
        viewModel.initialize(initialScenarioId)
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.onMicPermissionResult(granted)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onMicPermissionResult(granted)
        if (granted) viewModel.startListening()
    }

    LaunchedEffect(uiState.scenario?.id) {
        if (uiState.scenario != null) {
            viewModel.speakPrompt()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            AssistChip(onClick = {}, label = { Text("🔥 ${uiState.streak} day streak") })
        }

        Spacer(Modifier.height(12.dp))

        if (uiState.isLoadingScenario || uiState.scenario == null) {
            Spacer(Modifier.height(80.dp))
            CircularProgressIndicator()
        } else {
            val scenario = uiState.scenario!!

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(scenario.category) })
                AssistChip(onClick = {}, label = { Text(scenario.difficulty) })
            }

            Spacer(Modifier.height(16.dp))

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = scenario.promptText,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.speakPrompt() }) {
                        Icon(Icons.Filled.VolumeUp, contentDescription = "Replay prompt")
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text = statusText(uiState),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            MicButton(
                phase = uiState.phase,
                enabled = uiState.phase != Phase.PROCESSING,
                onClick = {
                    when (uiState.phase) {
                        Phase.IDLE -> {
                            if (uiState.hasMicPermission) {
                                viewModel.startListening()
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        }
                        Phase.LISTENING -> viewModel.cancelListening()
                        Phase.FEEDBACK_READY -> viewModel.tryAgain()
                        Phase.PROCESSING -> Unit
                    }
                }
            )

            if (uiState.phase == Phase.LISTENING) {
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = { viewModel.cancelListening() }) {
                    Text("Cancel")
                }
            }

            if (!uiState.hasMicPermission) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Microphone access is needed to practice speaking.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            uiState.errorMessage?.let { message ->
                Spacer(Modifier.height(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(message, color = MaterialTheme.colorScheme.onErrorContainer)
                        Spacer(Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.dismissError() }) { Text("OK") }
                    }
                }
            }

            if (uiState.transcript.isNotBlank()) {
                Spacer(Modifier.height(24.dp))
                TranscriptCard(transcript = uiState.transcript, struggledWords = uiState.struggledWords)
            }

            uiState.feedback?.let { feedback ->
                Spacer(Modifier.height(16.dp))
                FeedbackCard(feedback = feedback)

                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { viewModel.tryAgain() }) { Text("Try Again") }
                    Button(onClick = { viewModel.nextScenario() }) { Text("Next Scenario") }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

private fun statusText(uiState: PracticeUiState): String = when (uiState.phase) {
    Phase.IDLE -> if (uiState.feedback == null) {
        "Tap the microphone when you're ready to speak"
    } else {
        "Tap the microphone to try again"
    }
    Phase.LISTENING -> "Listening... take your time"
    Phase.PROCESSING -> "Processing your answer..."
    Phase.FEEDBACK_READY -> "Feedback ready"
}

@Composable
private fun TranscriptCard(transcript: String, struggledWords: Set<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("What you said", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(text = highlightedTranscript(transcript, struggledWords))
            if (struggledWords.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Highlighted words may need clearer pronunciation.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarningAmber
                )
            }
        }
    }
}

private fun highlightedTranscript(transcript: String, struggledWords: Set<String>) = buildAnnotatedString {
    val words = transcript.split(" ")
    words.forEachIndexed { index, word ->
        val bareWord = word.trim('.', ',', '!', '?').lowercase()
        if (struggledWords.contains(bareWord)) {
            withStyle(SpanStyle(color = WarningAmber, fontWeight = FontWeight.Bold)) {
                append(word)
            }
        } else {
            append(word)
        }
        if (index != words.lastIndex) append(" ")
    }
}

@Composable
private fun FeedbackCard(feedback: FeedbackResult) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Score", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Text(
                    "${feedback.score} / 100",
                    style = MaterialTheme.typography.titleLarge,
                    color = scoreColor(feedback.score)
                )
            }

            if (feedback.matchedPhrases.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text("Well said", style = MaterialTheme.typography.titleMedium, color = SuccessGreen)
                feedback.matchedPhrases.forEach { Text("• $it") }
            }

            if (feedback.missingPhrases.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text("Try to also include", style = MaterialTheme.typography.titleMedium, color = WarningAmber)
                feedback.missingPhrases.forEach { Text("• $it") }
            }

            if (feedback.struggledWords.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text("Words to pronounce more clearly", style = MaterialTheme.typography.titleMedium, color = AlertRed)
                Text(feedback.struggledWords.joinToString(", "))
            }
        }
    }
}

private fun scoreColor(score: Int) = when {
    score >= 80 -> SuccessGreen
    score >= 50 -> WarningAmber
    else -> AlertRed
}
