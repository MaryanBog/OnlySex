package com.onlysex.app.ui.screens.game

fun formatTaskText(
    text: String,
    partner1Name: String,
    partner2Name: String?,
    partner1Role: String?,
    partner2Role: String?
): String {
    if (partner2Name == null) {
        return text.replace(
            oldValue = "your partner",
            newValue = partner1Name,
            ignoreCase = true
        )
    }

    val observerName = when {
        partner1Role.equals("observer", ignoreCase = true) -> {
            partner1Name
        }

        partner2Role.equals("observer", ignoreCase = true) -> {
            partner2Name
        }

        else -> {
            partner2Name
        }
    }

    return text
        .replace(
            oldValue = "the observing partner",
            newValue = observerName,
            ignoreCase = true
        )
        .replace(
            oldValue = "the observer",
            newValue = observerName,
            ignoreCase = true
        )
        .replace(
            oldValue = "both partners",
            newValue = "$partner1Name and $partner2Name",
            ignoreCase = true
        )
        .replace(
            oldValue = "one of your partners",
            newValue = partner1Name,
            ignoreCase = true
        )
        .replace(
            oldValue = "one partner",
            newValue = partner1Name,
            ignoreCase = true
        )
        .replace(
            oldValue = "the other partner",
            newValue = partner2Name,
            ignoreCase = true
        )
        .replace(
            oldValue = "your partners",
            newValue = "$partner1Name and $partner2Name",
            ignoreCase = true
        )
        .replace(
            oldValue = "your partner",
            newValue = partner1Name,
            ignoreCase = true
        )
}