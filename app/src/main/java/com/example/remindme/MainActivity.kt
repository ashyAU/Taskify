package com.example.remindme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import java.util.Calendar


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParentComposable()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ParentComposable() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = navigationList[selectedIndex].label)
            },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
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

data class NavigationData(
    val iconUnfilled: Int = 0,
    val iconFilled: Int = 0,
    val label: String = "Error"
)

val navigationList = mutableListOf(
    NavigationData(R.drawable.alarm, R.drawable.alarm_filled, "Alarm"),
    NavigationData(R.drawable.tasks, R.drawable.tasks_filled, "Tasks"),
    NavigationData(R.drawable.stopwatch, R.drawable.stopwatch_filled, "Stopwatch"),
    NavigationData(R.drawable.countdown, R.drawable.countdown_filled, "Timer")
)





