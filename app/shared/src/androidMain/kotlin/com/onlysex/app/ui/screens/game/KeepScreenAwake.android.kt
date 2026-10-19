package com.onlysex.app.ui.screens.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView

@Composable
actual fun KeepScreenAwake() {
    val view = LocalView.current

    DisposableEffect(view) {
        val previousValue = view.keepScreenOn

        view.keepScreenOn = true

        onDispose {
            view.keepScreenOn = previousValue
        }
    }
}