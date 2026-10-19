package com.onlysex.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val OnlySexBackground = Color(0xFF12090F)
val OnlySexSurface = Color(0xFF21131B)
val OnlySexAccent = Color(0xFFFF3D7F)
val OnlySexText = Color(0xFFFFFFFF)
val OnlySexSecondaryText = Color(0xFFB9A7B0)
val OnlySexDanger = Color(0xFFFF1744)

private val OnlySexColorScheme = darkColorScheme(
    primary = OnlySexAccent,
    onPrimary = OnlySexText,
    background = OnlySexBackground,
    onBackground = OnlySexText,
    surface = OnlySexSurface,
    onSurface = OnlySexText,
    error = OnlySexDanger,
    onError = OnlySexText
)

@Composable
fun OnlySexTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OnlySexColorScheme,
        content = content
    )
}