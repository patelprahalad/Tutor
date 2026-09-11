# Vessel Tutor

An Android app that helps a Vessel Manager (or anyone in shipping ops) practice spoken
professional English for work situations: status updates, phone calls, meetings, explaining
problems, requests and confirmations.

## How it works

1. **Practice mode** — each scenario opens with a one-line situational **context** ("You're the
   Vessel Manager. The port agent just called...") spoken before the prompt, so it doesn't feel
   like a bare exam question. Answer freely, or switch to **Listen & Repeat** to hear a model
   sentence and try to repeat it back word for word — good for pronunciation drilling. Android's
   on-device `SpeechRecognizer` transcribes your answer, it's scored, and feedback is both spoken
   back and shown on screen.
2. **Pronunciation proxy** — `SpeechRecognizer` doesn't expose real per-word confidence, so as a
   rough proxy the app asks for several alternate transcriptions and flags words that most of
   those alternates disagree on (a majority vote, not "any single alternate" — the first version
   of this over-flagged almost every word). Those words are highlighted and tracked over time.
3. **Scenario library** — 30 starter scenarios across Status Updates, Phone Calls, Meetings,
   Explaining Problems, Requests, Confirmations, and **Daily Life** (small talk, ordering food,
   directions, calling family) — stored in a local Room database.
4. **Add Topic** — type a topic, snap or upload a photo, or upload a `.txt` document, and Claude
   (via the Anthropic API, using your own key) turns it into a new practice scenario under "My
   Topics." There's also a "surprise me" button for a fresh, topical scenario suggestion. Requires
   an Anthropic API key, entered once in Settings — see below.
5. **Progress dashboard** — daily streak, sessions completed, a score trend chart, and your most
   common recognizer-flagged words, all persisted locally in Room.

## Project structure (MVVM)

```
app/src/main/java/com/vesseltutor/app/
├── data/
│   ├── local/            Room entities, DAOs, AppDatabase
│   ├── repository/       ScenarioRepository, ProgressRepository
│   └── seed/              Starter scenario content
├── speech/                TextToSpeechManager, SpeechRecognizerManager, uncertainty analysis
├── feedback/               FeedbackEngine — compares transcript to expected phrases, scores it
├── network/                AnthropicClient — calls the Messages API to generate new scenarios
├── di/                     Tiny hand-rolled service locator + ViewModelFactory
└── ui/
    ├── practice/            Practice screen + ViewModel (free-response + listen & repeat)
    ├── library/             Scenario browser
    ├── progress/            Dashboard
    ├── addtopic/            Topic / photo / document → AI-generated scenario
    ├── settings/            Anthropic API key entry
    ├── components/          Shared composables (MicButton)
    ├── navigation/          NavHost + bottom navigation
    └── theme/                Compose Material3 theme
```

### Setting up the Add Topic feature

1. Get a free API key at [console.anthropic.com](https://console.anthropic.com/settings/keys).
2. In the app, tap the gear icon → paste the key → Save. It's stored encrypted on-device
   (`androidx.security` `EncryptedSharedPreferences`) and only ever sent directly to
   `api.anthropic.com`.
3. Tap the "+" icon to open Add Topic: type a topic, take/upload a photo, or upload a `.txt`
   file. Claude reads it and returns a scenario (context, prompt, key phrases, vocabulary, a
   sample answer) which is saved under the "My Topics" category and opened for practice.
4. "Surprise me with today's topic" asks Claude to suggest something fresh from its own
   knowledge — note this is not literal live web search, just Claude's general knowledge, so
   don't expect breaking-news-level freshness.

## Building

This was scaffolded in a sandboxed environment without the Android SDK, so it has **not**
been built or run here — only hand-verified for Kotlin/Gradle correctness. To build it:

1. Open the `VesselTutor/` folder in Android Studio (Koala/Ladybug or newer).
2. Let Gradle sync — it will download AGP 8.5.2, Kotlin 1.9.24, and the other dependencies
   listed in `app/build.gradle.kts`.
3. Run on a device or emulator running Android 8.0 (API 26) or newer, with a microphone and
   Google's on-device speech recognition available (any device with Google app / Google
   Speech Services installed — i.e. virtually any real device or a Play Store emulator image).
4. Grant microphone permission when prompted on first use of the mic button.

### Getting an installable APK without Android Studio

Pushing to `main` (or running the workflow manually) triggers
`.github/workflows/build-apk.yml`, which builds a debug APK on GitHub's runners (which come
with the Android SDK preinstalled) and uploads it as a workflow artifact.

1. Push this repo to GitHub.
2. Go to the repo's **Actions** tab → **Build Debug APK** → open the latest run (or click
   **Run workflow** to trigger it manually).
3. Download the `vessel-tutor-debug-apk` artifact once the run finishes (a few minutes), unzip
   it, and copy `app-debug.apk` to your phone (or `adb install app-debug.apk`).
4. On the phone, allow "Install unknown apps" for whichever app you used to open the file, then
   install it — this is a debug build, so no Play Store or signing setup is needed.

### Known environment constraints worth knowing about

- `RecognizerIntent.EXTRA_SPEECH_INPUT_*_SILENCE_LENGTH_MILLIS` extras are used to make the
  recognizer more patient with hesitant speech; they're honored by Google's recognizer service
  but are not guaranteed by all OEM recognizer implementations.
- Speech recognition and TTS both need the corresponding system services present — the
  `<queries>` block in `AndroidManifest.xml` declares them so the app can see them on API 30+.

## Extending it

- Swap the hand-rolled `AppContainer`/`ViewModelFactory` for Hilt once the app grows.
- `FeedbackEngine` is a simple phrase/word-overlap matcher — a natural next step is to swap in a
  more nuanced grammar checker.
- `ScenarioSeeder` is where to add more scenarios or new categories.
