package com.onlysex.app.sensor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSDate
import platform.Foundation.NSOperationQueue
import kotlin.math.sqrt

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ShakeDetector(
    enabled: Boolean,
    onShake: () -> Unit
) {
    val motionManager = remember {
        CMMotionManager()
    }

    val currentOnShake by rememberUpdatedState(onShake)

    DisposableEffect(enabled) {
        if (!enabled || !motionManager.accelerometerAvailable) {
            return@DisposableEffect onDispose {}
        }

        var lastShakeTime = 0.0

        motionManager.accelerometerUpdateInterval = 0.05

        motionManager.startAccelerometerUpdatesToQueue(
            NSOperationQueue.mainQueue
        ) { data, _ ->
            if (data != null) {
                val acceleration = data.acceleration.useContents {
                    sqrt(x * x + y * y + z * z)
                }

                val currentTime = NSDate().timeIntervalSince1970

                if (
                    acceleration > 2.7 &&
                    currentTime - lastShakeTime > 1.2
                ) {
                    lastShakeTime = currentTime
                    currentOnShake()
                }
            }
        }

        onDispose {
            motionManager.stopAccelerometerUpdates()
        }
    }
}