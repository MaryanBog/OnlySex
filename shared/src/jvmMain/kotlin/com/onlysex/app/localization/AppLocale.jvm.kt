package com.onlysex.app.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

actual object LocalAppLocale {

    private var defaultLocale: Locale? = null

    private val localLocale = staticCompositionLocalOf {
        Locale.getDefault().toLanguageTag()
    }

    actual val current: String
        @Composable get() = localLocale.current

    @Composable
    actual infix fun provides(
        value: String?
    ): ProvidedValue<*> {
        if (defaultLocale == null) {
            defaultLocale = Locale.getDefault()
        }

        val newLocale = if (value == null) {
            defaultLocale!!
        } else {
            Locale.forLanguageTag(value)
        }

        Locale.setDefault(newLocale)

        return localLocale.provides(
            newLocale.toLanguageTag()
        )
    }
}