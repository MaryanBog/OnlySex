package com.onlysex.app.alert

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.onlysex.app.shared.R

private lateinit var applicationContext: Context

fun initializeTimerAlert(context: Context) {
    applicationContext = context.applicationContext
}

@SuppressLint("MissingPermission")
actual fun playTimerAlert() {
    if (!::applicationContext.isInitialized) {
        return
    }

    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(
            AudioAttributes.CONTENT_TYPE_SONIFICATION
        )
        .build()

    val mediaPlayer = MediaPlayer.create(
        applicationContext,
        R.raw.timer_complete,
        audioAttributes,
        0
    )

    mediaPlayer?.apply {
        setVolume(1.0f, 1.0f)

        setOnCompletionListener { completedPlayer ->
            completedPlayer.release()
        }

        start()
    }

    val vibrationPattern = longArrayOf(
        0,
        400,
        150,
        400,
        150,
        700
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            applicationContext.getSystemService(
                Context.VIBRATOR_MANAGER_SERVICE
            ) as VibratorManager

        vibratorManager.defaultVibrator.vibrate(
            VibrationEffect.createWaveform(
                vibrationPattern,
                -1
            )
        )
    } else {
        @Suppress("DEPRECATION")
        val vibrator =
            applicationContext.getSystemService(
                Context.VIBRATOR_SERVICE
            ) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    vibrationPattern,
                    -1
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(
                vibrationPattern,
                -1
            )
        }
    }
}