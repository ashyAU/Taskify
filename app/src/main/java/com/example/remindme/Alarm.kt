package com.example.remindme

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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Calendar


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmParent() {
    var isOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlarmButton(onOpen = { isOpen = it })
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            alarmCard(isOpen = isOpen, onOpen = { isOpen = it })
        }
    }
}


@Composable
fun AlarmButton(
    onOpen: (Boolean) -> Unit
) {
    FilledTonalIconButton(
        onClick = {
            onOpen(true)
        },
        modifier = Modifier.size(110.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = null,
            modifier = Modifier.fillMaxSize(0.4f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun alarmCard(
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