package com.example.remindme.stopwatch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun StopWatchCounter(
    isStarted: Boolean,
    isReset: Boolean,
    onReset: (Boolean) -> Unit,
    updateCount: (Long) -> Unit,
    counter: Long,
    lastUpdatedTime: Long

) {
    var totalElapsedTime by remember {
        mutableLongStateOf(counter)
    }
    var lastStartTime by remember { mutableLongStateOf(lastUpdatedTime) }

    LaunchedEffect(isReset) {
        if (isReset) {
            totalElapsedTime = 0L
            lastStartTime = 0L
            onReset(false)
        }
    }

    LaunchedEffect(isStarted) {
        if (isStarted) {
            if (lastStartTime == 0L) {
                lastStartTime = System.currentTimeMillis()

            }

            while (true) {
                val currentTime = System.currentTimeMillis()
                totalElapsedTime += currentTime - lastStartTime
                lastStartTime = currentTime

                updateCount(totalElapsedTime)

                delay(100)
            }
        }
    }
    // todo, finish integrating the timer working when state is recomposed and paused.
    if (!isStarted ) {
        updateCount(totalElapsedTime)
    }
}