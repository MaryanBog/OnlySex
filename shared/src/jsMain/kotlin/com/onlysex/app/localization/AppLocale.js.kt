package com.onlysex.app.localization

package com.onlysex.app.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.intl.Locale

actual object LocalAppLocale {

    private val localLocale = staticCompositionLocalOf {
        Locale.current.toString()
    }

    actual val current: String
        @Composable get() = localLocale.current

    @Composable
    actual infix fun provides(
        value: String?
    ): ProvidedValue<*> {
        updateCustomLocale(
            value?.replace('_', '-')
        )

        return localLocale.provides(
            value ?: Locale.current.toString()
        )
    }
}

private fun updateCustomLocale(
    value: String?
) {
    js(
        """
        if (window.__customLocale !== value) {
            window.__customLocale = value;
            window.dispatchEvent(new Event("languagechange"));
        }
        """
    )
}