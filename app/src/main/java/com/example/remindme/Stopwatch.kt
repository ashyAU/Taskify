package com.example.remindme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime


@Preview
@Composable
fun StopwatchParent() {
    var isStarted by remember {
        mutableStateOf(false)
    }
    var counter by remember {
        mutableLongStateOf(0)
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // todo, add timer!
        StopwatchButton(isStarted = isStarted, onStart = { isStarted = it })
        StopWatchCounter(isStarted = isStarted, updateCount = { counter = it })

        // this is going to be the pretty print
        TimeConversion(counter = counter, isStarted = isStarted)
        Text("$counter")
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
                true -> painterResource(id = R.drawable.pause_filled)
                false -> painterResource(id = R.drawable.play_filled)
            },
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.5f)
        )
    }
}

@Composable
fun StopWatchCounter(
    isStarted: Boolean,
    updateCount: (Long) -> Unit
) {

    // need to store the value when timer is paused
    val initialTime by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    LaunchedEffect(isStarted) {
        while (isStarted) {
            delay(100)
            updateCount(System.currentTimeMillis() - initialTime)
        }
    }
}

data class PrettyTime(
    var millisecond: Long,
    var second: Long,
    var minute: Long,
    var hour: Long
)


@Preview
@Composable
@OptIn(ExperimentalTime::class)
fun TimeConversion(
    isStarted: Boolean,
    counter: Long
) {
    val prettyTime = remember { PrettyTime(0, 0, 0, 0) }

    val duration = Duration
    val totalMilliseconds = duration.convert(
        value = counter.toDouble(),
        sourceUnit = DurationUnit.MILLISECONDS,
        targetUnit = DurationUnit.MILLISECONDS
    ).toInt()

    prettyTime.millisecond = (totalMilliseconds % 1000).toLong()
    prettyTime.second = ((totalMilliseconds / 1000) % 60).toLong()
    prettyTime.minute = ((totalMilliseconds / 60000) % 60).toLong()
    prettyTime.hour = ((totalMilliseconds / 3600000).toLong())

    LaunchedEffect(isStarted) {
        while (isStarted) {
            delay(16)
            prettyTime.millisecond += 1

            if (prettyTime.millisecond >= 1000) {
                prettyTime.millisecond -= 1000
                prettyTime.second++
            }
            if (prettyTime.second >= 60) {
                prettyTime.second = 0
                prettyTime.minute++
            }
            if (prettyTime.minute >= 60) {
                prettyTime.minute = 0
                prettyTime.hour++
            }
        }
    }
    val formattedTime = String.format(locale = Locale.getDefault(),
        "%02d:%02d:%02d:%03d",
        prettyTime.hour,
        prettyTime.minute,
        prettyTime.second,
        prettyTime.millisecond
    )
    Text(text = formattedTime)
}





