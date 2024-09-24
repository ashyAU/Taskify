package com.example.remindme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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


@Preview
@Composable
fun StopwatchParent() {
    var isStarted by remember {
        mutableStateOf(false)
    }
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // todo, add timer!
        StopwatchButton(isStarted = isStarted, onStart = { isStarted = it })
    }
}

@Composable
fun StopwatchButton(
    isStarted: Boolean,
    onStart: (Boolean) -> Unit
) {
    FilledTonalIconButton(onClick = { onStart(!isStarted) }, modifier = Modifier.size(120.dp)) {
        Icon(
            painter =
            when (isStarted) {
                true -> painterResource(id = R.drawable.play_filled)
                false -> painterResource(id = R.drawable.pause_filled)
            },
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.5f)
        )
    }
}

@Composable
fun StopwatchTime(isStarted: Boolean, onStart: (Boolean) -> Unit): String {

    val startedTime by remember {
        mutableStateOf(Calendar.getInstance())
    }
    val currentTime = Calendar.getInstance()

    return ""
}