package com.vesseltutor.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Practice : Screen("practice", "Practice", Icons.Filled.Mic)
    data object Library : Screen("library", "Library", Icons.Filled.MenuBook)
    data object Progress : Screen("progress", "Progress", Icons.Filled.BarChart)

    companion object {
        val bottomNavItems = listOf(Practice, Library, Progress)
    }
}

const val PRACTICE_ROUTE_WITH_ARG = "practice/{scenarioId}"

fun practiceRoute(scenarioId: Long? = null): String =
    if (scenarioId != null) "practice/$scenarioId" else Screen.Practice.route
