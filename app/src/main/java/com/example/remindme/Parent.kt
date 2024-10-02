package com.example.remindme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.remindme.components.DropDownMenuMain
import com.example.remindme.setings.SettingsParent
import com.example.remindme.stopwatch.StopwatchParent

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ParentComposable() {


    var selectedIndex by remember { mutableIntStateOf(0) }
    var dropdownMenuOpen by remember { mutableStateOf(false) }

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    DropDownMenuMain(
        dropdownMenuOpen = dropdownMenuOpen,
        isOpen = { dropdownMenuOpen = it },
        navController = navController
    )

    Scaffold(topBar = {

        TopAppBar(title = {
            Text(text = navigationList[selectedIndex].label)
        }, actions = {
            IconButton(onClick = {
                dropdownMenuOpen = !dropdownMenuOpen
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = null
                )
            }
        }
        )
    }, bottomBar = {
        if (currentRoute != AppRoute.Settings.route) {
            NavigationBar {
                navigationList.forEachIndexed { index, navigationList ->
                    if (index != 4) {
                        NavigationBarItem(
                            selected = currentRoute == navigationList.route.route,
                            icon = {
                                Icon(
                                    painter = painterResource(id = if (selectedIndex != index) navigationList.iconUnfilled else navigationList.iconFilled),
                                    contentDescription = navigationList.label
                                )
                            },
                            label = { Text(navigationList.label) },
                            onClick = {
                                selectedIndex = index
                                navController.navigate(navigationList.route.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }

            }
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Alarm.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoute.Alarm.route) { AlarmParent() }
            composable(AppRoute.Tasks.route) { TasksParent() }
            composable(AppRoute.Stopwatch.route) { navBackStackEntry ->
                StopwatchParent(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry
                )
            }
            composable(AppRoute.Timer.route) { TasksParent() }
            composable(AppRoute.Settings.route) { SettingsParent() }
        }
    }
}


data class NavigationData<T : Any>(
    val iconUnfilled: Int, val iconFilled: Int, val label: String, val route: T
)

val navigationList = listOf(
    NavigationData(R.drawable.alarm, R.drawable.alarm_filled, "Alarm", AppRoute.Alarm),
    NavigationData(R.drawable.tasks, R.drawable.tasks_filled, "Tasks", AppRoute.Tasks),
    NavigationData(
        R.drawable.stopwatch, R.drawable.stopwatch_filled, "Stopwatch", AppRoute.Stopwatch
    ),
    NavigationData(R.drawable.countdown, R.drawable.countdown_filled, "Timer", AppRoute.Timer),
    NavigationData(R.drawable.settings, R.drawable.settings, "Settings", AppRoute.Settings)
)

sealed class AppRoute(val route: String) {
    data object Alarm : AppRoute("alarm")
    data object Tasks : AppRoute("tasks")
    data object Stopwatch : AppRoute("stopwatch")
    data object Timer : AppRoute("timer")
    data object Settings : AppRoute("settings")
}