package com.example.remindme

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime


@Preview(showBackground = true)
@Composable
fun StopwatchParent() {
    var isStarted by rememberSaveable {
        mutableStateOf(false)
    }
    var counter by rememberSaveable {
        mutableLongStateOf(0)
    }
    var isReset by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StopWatchCounter(
            isStarted = isStarted,
            isReset = isReset,
            updateCount = { counter = it },
            onReset = { isReset = it })

        // this is going to be the pretty print
        Timer(counter = counter, isStarted = isStarted)
        StopwatchButtons(
            isStarted = isStarted,
            onStart = { isStarted = it },
            onReset = { isReset = it },
        )
    }
}

@Composable
fun StopwatchButtons(
    isStarted: Boolean,
    onStart: (Boolean) -> Unit,
    onReset: (Boolean) -> Unit,
) {
    val icon = if (isStarted) R.drawable.pause_filled else R.drawable.play_filled
    val size by animateDpAsState(targetValue = if (isStarted) 160.dp else 110.dp, label = "")
    Row(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        FilledTonalIconButton(onClick = {
            onReset(true)
            onStart(false)
        }, modifier = Modifier.size(75.dp)) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
        }
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        FilledTonalIconButton(
            onClick = { onStart(!isStarted) },
            modifier = Modifier.size(
                width = size, height = 110.dp
            )
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.4f)
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        FilledTonalIconButton(
            onClick = {},
            modifier = Modifier
                .size(75.dp)
                .alpha(if (isStarted) 1f else 0f)
        ) {
            Icon(painter = painterResource(id = R.drawable.stopwatch), contentDescription = null)
        }
    }
}

@Composable
fun StopWatchCounter(
    isStarted: Boolean,
    isReset: Boolean,
    onReset: (Boolean) -> Unit,
    updateCount: (Long) -> Unit

) {
    var totalElapsedTime by remember { mutableLongStateOf(0L) }
    var lastStartTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isReset) {
        totalElapsedTime = 0L
        lastStartTime = 0L
        onReset(false)
    }

    LaunchedEffect(isStarted) {
        if (isStarted) {
            lastStartTime = System.currentTimeMillis()
            while (true) {
                val currentTime = System.currentTimeMillis()
                totalElapsedTime += currentTime - lastStartTime
                lastStartTime = currentTime

                updateCount(totalElapsedTime)

                delay(100)
            }
        } else {
            lastStartTime = 0L
        }
    }
    if (!isStarted) {
        updateCount(totalElapsedTime)
    }
}


data class PrettyTime(
    var millisecond: Long,
    var second: Long,
    var minute: Long,
    var hour: Long
)

@Composable
@OptIn(ExperimentalTime::class)
fun Timer(
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
    val formattedTime = String.format(
        locale = Locale.getDefault(),
        "%02d : %02d : %02d",
        prettyTime.hour,
        prettyTime.minute,
        prettyTime.second,
        prettyTime.millisecond
    )
    Text(
        text = formattedTime,
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.Normal
    )
}





