package com.onlysex.app.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlin.math.sqrt

@Composable
actual fun ShakeDetector(
    enabled: Boolean,
    onShake: () -> Unit
) {
    val context = LocalContext.current
    val currentOnShake by rememberUpdatedState(onShake)

    DisposableEffect(enabled, context) {
        if (!enabled) {
            return@DisposableEffect onDispose {}
        }

        val sensorManager = context.getSystemService(
            Context.SENSOR_SERVICE
        ) as SensorManager

        val accelerometer = sensorManager.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER
        )

        var lastShakeTime = 0L

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val acceleration = sqrt(
                    x * x + y * y + z * z
                ) / SensorManager.GRAVITY_EARTH

                val currentTime = SystemClock.elapsedRealtime()

                if (
                    acceleration > 2.7f &&
                    currentTime - lastShakeTime > 1_200L
                ) {
                    lastShakeTime = currentTime
                    currentOnShake()
                }
            }

            override fun onAccuracyChanged(
                sensor: Sensor?,
                accuracy: Int
            ) {
                // No action required.
            }
        }

        if (accelerometer != null) {
            sensorManager.registerListener(
                listener,
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
}