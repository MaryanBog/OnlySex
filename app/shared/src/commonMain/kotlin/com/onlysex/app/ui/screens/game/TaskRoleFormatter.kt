package com.onlysex.app.ui.screens.game

import androidx.compose.runtime.Composable
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.role_observer
import onlysex.shared.generated.resources.role_participant
import onlysex.shared.generated.resources.role_performer
import onlysex.shared.generated.resources.role_receiver
import onlysex.shared.generated.resources.role_takes_turns
import org.jetbrains.compose.resources.stringResource

@Composable
fun formatTaskRole(role: String?): String {
    if (role.isNullOrBlank()) {
        return stringResource(Res.string.role_participant)
    }

    return when (role.lowercase()) {
        "active",
        "active_player",
        "giver",
        "performer" -> {
            stringResource(Res.string.role_performer)
        }

        "partner",
        "receiver",
        "recipient" -> {
            stringResource(Res.string.role_receiver)
        }

        "observer",
        "watcher" -> {
            stringResource(Res.string.role_observer)
        }

        "mutual" -> {
            stringResource(Res.string.role_takes_turns)
        }

        else -> {
            role
                .replace("_", " ")
                .replace("-", " ")
                .uppercase()
        }
    }
}