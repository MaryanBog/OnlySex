package com.onlysex.app.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

actual object LocalAppLocale {

    private const val LANGUAGE_KEY = "AppleLanguages"

    private val defaultLocale =
        NSLocale.preferredLanguages.first() as String

    private val localLocale = staticCompositionLocalOf {
        defaultLocale
    }

    actual val current: String
        @Composable get() = localLocale.current

    @Composable
    actual infix fun provides(
        value: String?
    ): ProvidedValue<*> {
        val newLocale = value ?: defaultLocale

        if (value == null) {
            NSUserDefaults.standardUserDefaults
                .removeObjectForKey(LANGUAGE_KEY)
        } else {
            NSUserDefaults.standardUserDefaults.setObject(
                arrayListOf(newLocale),
                forKey = LANGUAGE_KEY
            )
        }

        return localLocale.provides(newLocale)
    }
}