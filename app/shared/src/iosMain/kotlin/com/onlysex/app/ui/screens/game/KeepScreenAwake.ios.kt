package com.onlysex.app.ui.screens.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun KeepScreenAwake() {
    DisposableEffect(Unit) {
        val application =
            UIApplication.sharedApplication

        val previousValue =
            application.idleTimerDisabled

        application.idleTimerDisabled = true

        onDispose {
            application.idleTimerDisabled =
                previousValue
        }
    }
}