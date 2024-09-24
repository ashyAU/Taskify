package com.example.remindme

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
        StopWatchCounter(isStarted = isStarted, updateCount = { counter = it })

        // this is going to be the pretty print
        Timer(counter = counter, isStarted = isStarted)
        StopwatchButtons(isStarted = isStarted, onStart = { isStarted = it })

    }
}


// do tomorrow,
@Composable
fun StopwatchButtons(
    isStarted: Boolean,
    onStart: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround
    )
    {
        Box(modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center)
        {
            FilledTonalIconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(60.dp)) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }
        }
        when (isStarted) {
            true -> {
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center)
                {
                    FilledTonalIconButton(
                        onClick = { onStart(!isStarted) },
                        modifier = Modifier.size(width = 140.dp, height = 100.dp)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pause_filled),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(0.5f)
                        )
                    }
                }
            }

            false -> {
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center)
                {
                    FilledTonalIconButton(
                        onClick = { onStart(!isStarted) },
                        modifier = Modifier.size(100.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.play_filled),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(0.5f)
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = isStarted,
            enter = fadeIn(),
            exit = fadeOut()
        )
        {
            Box(modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center)
            {
                FilledTonalIconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Place, contentDescription = null)
                }
            }
        }
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
        "%02d : %02d : %02d : %03d",
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





