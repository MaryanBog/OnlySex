package com.onlysex.app.sensor

import androidx.compose.runtime.Composable

@Composable
expect fun ShakeDetector(
    enabled: Boolean,
    onShake: () -> Unit
)