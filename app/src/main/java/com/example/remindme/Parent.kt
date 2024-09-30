package com.example.remindme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.remindme.components.DropDownMenuMain
import com.example.remindme.components.Menu
import com.example.remindme.setings.Placeholder
import com.example.remindme.stopwatch.StopwatchParent

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ParentComposable() {

    var selectedIndex by remember { mutableIntStateOf(0) }
    var dropdownMenuOpen by remember { mutableStateOf(false) }
    var index by rememberSaveable {
        mutableStateOf<Menu?>(Menu.default)
    }

    when (index) {
        Menu.settings -> {
            AnimatedVisibility(
                visible = index == Menu.settings,
                enter = slideInHorizontally(animationSpec = tween(durationMillis = 300)),
                exit = slideOutVertically(animationSpec = tween(durationMillis = 300))
            ) {
                Placeholder(index = { index = it })
            }
        }
        Menu.default -> {
            DropDownMenuMain(
                dropdownMenuOpen = dropdownMenuOpen,
                isOpen = { dropdownMenuOpen = it }, index = { index = it })

            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(text = navigationList[selectedIndex].label)
                    },
                        actions = {
                            IconButton(onClick = {
                                dropdownMenuOpen = !dropdownMenuOpen
                            }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                        }

                    )
                },

                content = { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        when (selectedIndex) {
                            0 -> AlarmParent()
                            1 -> TasksParent()
                            2 -> StopwatchParent()
                        }
                    }
                },
                bottomBar = {
                    NavigationBar {
                        navigationList.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = {
                                    if (selectedIndex == index) {
                                        Icon(
                                            painterResource(id = item.iconFilled),
                                            contentDescription = item.label
                                        )
                                    } else {
                                        Icon(
                                            painterResource(id = item.iconUnfilled),
                                            contentDescription = item.label
                                        )
                                    }
                                },
                                label = { Text(item.label) },
                                selected = selectedIndex == index,
                                onClick = { selectedIndex = index })
                        }
                    }
                }
            )
        }

        null -> TODO()
    }
}

data class NavigationData(
    val iconUnfilled: Int = 0,
    val iconFilled: Int = 0,
    val label: String = "Error"
)

val navigationList = listOf(
    NavigationData(R.drawable.alarm, R.drawable.alarm_filled, "Alarm"),
    NavigationData(R.drawable.tasks, R.drawable.tasks_filled, "Tasks"),
    NavigationData(R.drawable.stopwatch, R.drawable.stopwatch_filled, "Stopwatch"),
    NavigationData(R.drawable.countdown, R.drawable.countdown_filled, "Timer")
)