package com.onlysex.app.ui.screens.game

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.continue_game
import onlysex.shared.generated.resources.stop_dialog_description
import onlysex.shared.generated.resources.stop_dialog_title
import onlysex.shared.generated.resources.stop_game
import org.jetbrains.compose.resources.stringResource

@Composable
fun StopGameDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    Res.string.stop_dialog_title
                ),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(
                    Res.string.stop_dialog_description
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(Res.string.stop_game),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(
                        Res.string.continue_game
                    )
                )
            }
        }
    )
}