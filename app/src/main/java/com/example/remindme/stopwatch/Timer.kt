package com.example.remindme.stopwatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.remindme.database.StopwatchViewModel
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@Composable
@OptIn(ExperimentalTime::class)
fun Timer(
    isStarted: Boolean,
    counter: Long,
    stopwatchViewModel: StopwatchViewModel,
    isLap: Boolean,
    onLap: (Boolean) -> Unit

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

    // region Timer Formatting
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
        "%02d:%02d:%02d",
        prettyTime.hour,
        prettyTime.minute,
        prettyTime.second,
    )
    val formattedMS = String.format(
        locale = Locale.getDefault(),
        format = ".%03d",
        prettyTime.millisecond
    )
    // endregion

    // region Timer Layout
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center
    )
    {
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        )
        {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            )
            {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                )
                {
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = formattedMS,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
    // endregion

    // todo add the correct calculation for the time difference between the current lap and the previous lap.
    // this may require using previous indexing for calculations unless NULL
    if (isLap) {
        stopwatchViewModel.addLap(time = "$counter")
        onLap(false)
    }
}