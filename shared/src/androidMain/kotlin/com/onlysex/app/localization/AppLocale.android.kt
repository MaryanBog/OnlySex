package com.onlysex.app.localization

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

actual object LocalAppLocale {

    private var defaultLocale: Locale? = null

    actual val current: String
        @Composable get() = LocalConfiguration.current
            .locales[0]
            .toLanguageTag()

    @Composable
    actual infix fun provides(
        value: String?
    ): ProvidedValue<*> {
        val currentConfiguration =
            LocalConfiguration.current

        if (defaultLocale == null) {
            defaultLocale =
                currentConfiguration.locales[0]
        }

        val newLocale = if (value == null) {
            defaultLocale!!
        } else {
            Locale.forLanguageTag(value)
        }

        Locale.setDefault(newLocale)

        val newConfiguration = Configuration(
            currentConfiguration
        ).apply {
            setLocale(newLocale)
        }

        val resources = LocalContext.current.resources

        @Suppress("DEPRECATION")
        resources.updateConfiguration(
            newConfiguration,
            resources.displayMetrics
        )

        return LocalConfiguration.provides(
            newConfiguration
        )
    }
}