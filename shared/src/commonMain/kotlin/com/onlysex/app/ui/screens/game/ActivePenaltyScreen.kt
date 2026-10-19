package com.onlysex.app.ui.screens.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onlysex.app.model.Penalty
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.active_penalty_title
import onlysex.shared.generated.resources.active_player_role
import onlysex.shared.generated.resources.penalty_partner_role
import onlysex.shared.generated.resources.task_done
import onlysex.shared.generated.resources.task_start
import onlysex.shared.generated.resources.task_stop
import onlysex.shared.generated.resources.task_time_is_up
import onlysex.shared.generated.resources.task_wait
import onlysex.shared.generated.resources.two_player_names
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActivePenaltyScreen(
    activePlayerName: String,
    recipient1Name: String,
    recipient2Name: String?,
    penalty: Penalty,
    onDone: () -> Unit,
    onStop: () -> Unit
) {
    var timerStarted by remember(penalty.id) {
        mutableStateOf(false)
    }

    var timerFinished by remember(penalty.id) {
        mutableStateOf(penalty.durationSeconds == null)
    }

    val activePlayerRole = stringResource(
        Res.string.active_player_role
    )

    val partnerRole = stringResource(
        Res.string.penalty_partner_role
    )

    val bothRecipients = if (recipient2Name != null) {
        stringResource(
            Res.string.two_player_names,
            recipient1Name,
            recipient2Name
        )
    } else {
        recipient1Name
    }

    val formattedPenaltyText = penalty.text
        .replace(
            oldValue = "both partners",
            newValue = bothRecipients,
            ignoreCase = true
        )
        .replace(
            oldValue = "one partner",
            newValue = recipient1Name,
            ignoreCase = true
        )
        .replace(
            oldValue = "the other",
            newValue = recipient2Name ?: recipient1Name,
            ignoreCase = true
        )
        .replace(
            oldValue = "your partner",
            newValue = recipient1Name,
            ignoreCase = true
        )

    val formattedSafetyNote = penalty.safetyNote
        ?.replace(
            oldValue = "both partners",
            newValue = bothRecipients,
            ignoreCase = true
        )
        ?.replace(
            oldValue = "your partner",
            newValue = recipient1Name,
            ignoreCase = true
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                Res.string.active_penalty_title
            ),
            color = MaterialTheme.colorScheme.error,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "$activePlayerName — $activePlayerRole",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "$recipient1Name — $partnerRole",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

        if (recipient2Name != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$recipient2Name — $partnerRole",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = formattedPenaltyText,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        penalty.durationSeconds?.let { seconds ->
            Spacer(modifier = Modifier.height(24.dp))

            when {
                !timerStarted -> {
                    Button(
                        onClick = {
                            timerStarted = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = stringResource(
                                Res.string.task_start
                            ),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                !timerFinished -> {
                    CountdownTimer(
                        totalSeconds = seconds,
                        onFinished = {
                            timerFinished = true
                        }
                    )
                }

                else -> {
                    Text(
                        text = stringResource(
                            Res.string.task_time_is_up
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        formattedSafetyNote
            ?.takeIf { it.isNotBlank() }
            ?.let { note ->
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = note,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onDone,
            enabled = timerFinished,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = if (timerFinished) {
                    stringResource(Res.string.task_done)
                } else {
                    stringResource(Res.string.task_wait)
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onStop,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = stringResource(Res.string.task_stop),
                fontWeight = FontWeight.Bold
            )
        }
    }
}