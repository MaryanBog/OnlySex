package com.onlysex.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.onlysex.app.di.initKoin

fun main() {
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP-App-Template",
        ) {
            App()
        }
    }
}
