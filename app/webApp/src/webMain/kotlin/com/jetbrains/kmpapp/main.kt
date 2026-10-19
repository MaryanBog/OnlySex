package com.onlysex.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.onlysex.app.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()

    ComposeViewport(viewportContainerId = "composeTarget") {
        App()
    }
}
