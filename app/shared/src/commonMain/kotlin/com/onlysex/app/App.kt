package com.onlysex.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.onlysex.app.localization.AppEnvironment
import com.onlysex.app.navigation.AppNavigation
import com.onlysex.app.ui.theme.OnlySexTheme

@Composable
fun App() {
    AppEnvironment {
        OnlySexTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}