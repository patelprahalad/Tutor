package com.vesseltutor.app.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vesseltutor.app.data.local.entity.MistakeWordEntity
import com.vesseltutor.app.data.local.entity.PracticeSessionEntity
import com.vesseltutor.app.ui.theme.AlertRed
import com.vesseltutor.app.ui.theme.SuccessGreen
import com.vesseltutor.app.ui.theme.WarningAmber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProgressScreen(viewModel: ProgressViewModel) {
    val streak by viewModel.streak.collectAsState()
    val sessions by viewModel.recentSessions.collectAsState()
    val mistakes by viewModel.topMistakes.collectAsState()

    val averageScore = if (sessions.isNotEmpty()) sessions.map { it.score }.average() else 0.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(title = "Streak", value = "${streak?.currentStreak ?: 0}d", modifier = Modifier.weight(1f))
                StatCard(title = "Sessions", value = "${streak?.totalSessions ?: 0}", modifier = Modifier.weight(1f))
                StatCard(title = "Avg Score", value = averageScore.toInt().toString(), modifier = Modifier.weight(1f))
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Score trend (oldest to newest)", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    ScoreTrendChart(scores = sessions.take(15).map { it.score }.reversed())
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Most common mistakes", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    if (mistakes.isEmpty()) {
                        Text(
                            "No recurring mistakes yet — keep practicing!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        MistakesList(mistakes)
                    }
                }
            }
        }

        item {
            Text("Recent sessions", style = MaterialTheme.typography.titleMedium)
        }

        if (sessions.isEmpty()) {
            item {
                Text(
                    "Complete a practice scenario to see your history here.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        items(sessions, key = { it.id }) { session ->
            SessionRow(session)
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ScoreTrendChart(scores: List<Int>) {
    if (scores.isEmpty()) {
        Text("No sessions yet.", style = MaterialTheme.typography.bodyMedium)
        return
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        scores.forEach { score ->
            val fraction = (score.coerceIn(0, 100) / 100f).coerceAtLeast(0.05f)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(fraction)
                    .background(trendColor(score), shape = RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
private fun MistakesList(mistakes: List<MistakeWordEntity>) {
    Column {
        mistakes.forEach { mistake ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(mistake.word, style = MaterialTheme.typography.bodyLarge)
                Text("${mistake.occurrences}×", style = MaterialTheme.typography.bodyMedium, color = WarningAmber)
            }
        }
    }
}

@Composable
private fun SessionRow(session: PracticeSessionEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(session.category, style = MaterialTheme.typography.labelLarge)
                Text(
                    text = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(session.timestamp)),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                "${session.score}",
                style = MaterialTheme.typography.titleMedium,
                color = trendColor(session.score)
            )
        }
    }
}

private fun trendColor(score: Int): Color = when {
    score >= 80 -> SuccessGreen
    score >= 50 -> WarningAmber
    else -> AlertRed
}
