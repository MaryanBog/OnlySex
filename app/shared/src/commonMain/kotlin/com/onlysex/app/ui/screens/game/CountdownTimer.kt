package com.onlysex.app.ui.screens.game

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.onlysex.app.alert.playTimerAlert
import kotlinx.coroutines.delay

@Composable
fun CountdownTimer(
    totalSeconds: Int,
    onFinished: () -> Unit = {}
) {
    var remainingSeconds by remember(totalSeconds) {
        mutableIntStateOf(totalSeconds)
    }

    LaunchedEffect(totalSeconds) {
        while (remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds--
        }

        playTimerAlert()
        onFinished()
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    val formattedTime =
        "$minutes:${seconds.toString().padStart(2, '0')}"

    Text(
        text = formattedTime,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )
}