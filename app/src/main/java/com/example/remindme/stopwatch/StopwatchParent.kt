package com.example.remindme.stopwatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.remindme.database.StopwatchViewModel


@Composable
fun StopwatchParent(navBackStackEntry: NavBackStackEntry) {

    val stopwatchViewModel: StopwatchViewModel = hiltViewModel(navBackStackEntry)

    val laps by stopwatchViewModel.allLaps.collectAsState(initial = emptyList())
    var isStarted by remember {
        mutableStateOf(false)
    }
    var counter by remember {
        mutableStateOf<Long?>(null)
    }
    var isReset by remember {
        mutableStateOf(false)
    }
    var isLap by remember {
        mutableStateOf(false)
    }
    var lastUpdatedTime by remember {
        mutableStateOf<Long?>(null)
    }
    LaunchedEffect(Unit) {
        stopwatchViewModel.getLastStopwatchValue { lastValue ->
            lastValue.let {
                if (it != null) {
                    counter = it.counterValue
                    isStarted = it.isStarted
                    lastUpdatedTime = it.lastUpdatedTime
                } else {
                    counter = 0
                }

            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            counter?.let { counter ->
                stopwatchViewModel.saveStopwatchValue(
                    counterValue = counter,
                    lastUpdatedTime = System.currentTimeMillis(),
                    isStarted = isStarted
                )
            }
        }
    }


    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        counter?.let { it ->
            Timer(
                counter = it,
                isStarted = isStarted,
                stopwatchViewModel = stopwatchViewModel,
                isLap = isLap,
                onLap = { isLap = it })

            lastUpdatedTime?.let { lastUpdate ->
                StopWatchCounter(
                    isStarted = isStarted,
                    isReset = isReset,
                    updateCount = { counter = it },
                    onReset = { isReset = it },
                    counter = it,
                    lastUpdatedTime = lastUpdate
                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth().padding(40.dp).weight(0.3f), contentAlignment = Alignment.Center) {
            LazyColumn {
                // todo, fix the formatting of the pretty print
                items(laps) { lap ->
                    Text(
                        text = "# ${lap.id}: ${lap.time}",
                        style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        StopwatchButtons(
            isStarted = isStarted,
            onStart = { isStarted = it },
            onReset = { isReset = it },
            stopwatchViewModel = stopwatchViewModel,
            onLap = { isLap = it }
        )
    }
}