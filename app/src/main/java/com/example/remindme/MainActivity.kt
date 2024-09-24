package com.example.remindme

import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.sql.Time
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
        }
    }
}


// Add page
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun event(
    isOpen: Boolean,
    onOpen: (Boolean) -> Unit
): TimePickerState? {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        currentTime.get(Calendar.HOUR_OF_DAY),
        currentTime.get(Calendar.MINUTE),
        false
    )
    var buttonClicked by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isOpen) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Set time",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                    TimePicker(state = timePickerState)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                onOpen(false)
                                buttonClicked = "Cancel"
                            }) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            onOpen(false)
                            buttonClicked = "OK"
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }

    return when (buttonClicked) {
        "OK" -> timePickerState
        "Cancel" -> null
        else -> null
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Preview
@Composable
fun ParentComposable() {
    var isOpen by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        floatingActionButton = {
            AddEvent(onOpen = { isOpen = it })
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                when(selectedIndex)
                {
                    2 -> StopwatchParent()
                }

                /* todo this is where the cards depending on the current state will be */
            }
        },
        bottomBar = {
            NavigationBar {
                navigationList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(id = item.iconUnfilled),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index })
                }
            }

        }
    )
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            event(isOpen = isOpen, onOpen = { isOpen = it })
        }
    }
}

@Composable
fun AddEvent(
    onOpen: (Boolean) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onOpen(true)
        }
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
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
    NavigationData(R.drawable.settings, R.drawable.settings_filled, "Settings")
)





