package com.vesseltutor.app.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vesseltutor.app.data.local.entity.ScenarioEntity
import com.vesseltutor.app.data.seed.ScenarioCategory

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onScenarioSelected: (Long) -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val scenarios by viewModel.scenarios.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.categories) { category ->
                FilterChip(
                    selected = category == selectedCategory,
                    onClick = { viewModel.selectCategory(category) },
                    label = { Text(category) }
                )
            }
        }

        if (scenarios.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = if (selectedCategory == ScenarioCategory.MY_TOPICS) {
                        "No topics yet. Tap the + icon at the top to create one from a topic, photo, or document."
                    } else {
                        "No scenarios in this category yet."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(scenarios, key = { it.id }) { scenario ->
                    ScenarioListItem(scenario = scenario, onClick = { onScenarioSelected(scenario.id) })
                }
            }
        }
    }
}

@Composable
private fun ScenarioListItem(scenario: ScenarioEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(scenario.difficulty, style = MaterialTheme.typography.labelLarge)
            Text(scenario.promptText, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
