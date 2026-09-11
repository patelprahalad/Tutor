package com.vesseltutor.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vesseltutor.app.VesselTutorApp
import com.vesseltutor.app.di.ViewModelFactory
import com.vesseltutor.app.ui.addtopic.AddTopicScreen
import com.vesseltutor.app.ui.addtopic.AddTopicViewModel
import com.vesseltutor.app.ui.library.LibraryScreen
import com.vesseltutor.app.ui.library.LibraryViewModel
import com.vesseltutor.app.ui.practice.PracticeScreen
import com.vesseltutor.app.ui.practice.PracticeViewModel
import com.vesseltutor.app.ui.progress.ProgressScreen
import com.vesseltutor.app.ui.progress.ProgressViewModel
import com.vesseltutor.app.ui.settings.SettingsScreen
import com.vesseltutor.app.ui.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(app: VesselTutorApp) {
    val navController = rememberNavController()
    val factory = ViewModelFactory(app)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vessel Tutor") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.AddTopic.route) { launchSingleTop = true }
                    }) {
                        Icon(Screen.AddTopic.icon, contentDescription = Screen.AddTopic.label)
                    }
                    IconButton(onClick = {
                        navController.navigate(Screen.Settings.route) { launchSingleTop = true }
                    }) {
                        Icon(Screen.Settings.icon, contentDescription = Screen.Settings.label)
                    }
                }
            )
        },
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination

            NavigationBar {
                Screen.bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == screen.route || (screen == Screen.Practice && it.route == PRACTICE_ROUTE_WITH_ARG)
                    } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Practice.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Practice.route) {
                val viewModel: PracticeViewModel = viewModel(factory = factory)
                PracticeScreen(viewModel = viewModel, initialScenarioId = null)
            }

            composable(
                route = PRACTICE_ROUTE_WITH_ARG,
                arguments = listOf(navArgument("scenarioId") { type = NavType.LongType })
            ) { backStackEntry ->
                val scenarioId = backStackEntry.arguments?.getLong("scenarioId")
                val viewModel: PracticeViewModel = viewModel(factory = factory)
                PracticeScreen(viewModel = viewModel, initialScenarioId = scenarioId)
            }

            composable(Screen.Library.route) {
                val viewModel: LibraryViewModel = viewModel(factory = factory)
                LibraryScreen(
                    viewModel = viewModel,
                    onScenarioSelected = { scenarioId ->
                        navController.navigate(practiceRoute(scenarioId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.Progress.route) {
                val viewModel: ProgressViewModel = viewModel(factory = factory)
                ProgressScreen(viewModel = viewModel)
            }

            composable(Screen.AddTopic.route) {
                val viewModel: AddTopicViewModel = viewModel(factory = factory)
                AddTopicScreen(
                    viewModel = viewModel,
                    onScenarioReady = { scenarioId ->
                        navController.navigate(practiceRoute(scenarioId)) {
                            launchSingleTop = true
                        }
                    },
                    onOpenSettings = {
                        navController.navigate(Screen.Settings.route) { launchSingleTop = true }
                    }
                )
            }

            composable(Screen.Settings.route) {
                val viewModel: SettingsViewModel = viewModel(factory = factory)
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
