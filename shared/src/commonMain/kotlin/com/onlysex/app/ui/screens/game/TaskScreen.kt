package com.onlysex.app.ui.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.onlysex.app.model.GameTask
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.intensity_level
import onlysex.shared.generated.resources.task_done
import onlysex.shared.generated.resources.task_pass
import onlysex.shared.generated.resources.task_start
import onlysex.shared.generated.resources.task_stop
import onlysex.shared.generated.resources.task_time_is_up
import onlysex.shared.generated.resources.task_wait
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskScreen(
    task: GameTask,
    activePlayerName: String,
    partner1Name: String,
    partner2Name: String?,
    onDone: () -> Unit,
    onPass: () -> Unit,
    onStop: () -> Unit
) {
    var timerStarted by remember(task.id) {
        mutableStateOf(false)
    }

    var timerFinished by remember(task.id) {
        mutableStateOf(task.durationSeconds == null)
    }

    val receiverName = when {
        task.activeRole.equals(
            "receiver",
            ignoreCase = true
        ) || task.activeRole.equals(
            "recipient",
            ignoreCase = true
        ) -> {
            activePlayerName
        }

        task.partner1Role.equals(
            "receiver",
            ignoreCase = true
        ) || task.partner1Role.equals(
            "recipient",
            ignoreCase = true
        ) -> {
            partner1Name
        }

        task.partner2Role.equals(
            "receiver",
            ignoreCase = true
        ) || task.partner2Role.equals(
            "recipient",
            ignoreCase = true
        ) -> {
            partner2Name
        }

        else -> {
            partner1Name
        }
    }

    val observerName = when {
        task.activeRole.equals(
            "observer",
            ignoreCase = true
        ) -> {
            activePlayerName
        }

        task.partner1Role.equals(
            "observer",
            ignoreCase = true
        ) -> {
            partner1Name
        }

        task.partner2Role.equals(
            "observer",
            ignoreCase = true
        ) -> {
            partner2Name
        }

        else -> {
            null
        }
    }

    val formattedSafetyNote = task.safetyNote
        ?.replace(
            oldValue = "the receiving partner",
            newValue = receiverName ?: partner1Name,
            ignoreCase = true
        )
        ?.replace(
            oldValue = "the receiver",
            newValue = receiverName ?: partner1Name,
            ignoreCase = true
        )
        ?.let { note ->
            if (observerName != null) {
                note
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
            } else {
                note
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                Res.string.intensity_level,
                task.level
            ),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "$activePlayerName — ${
                formatTaskRole(task.activeRole)
            }",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "$partner1Name — ${
                formatTaskRole(task.partner1Role)
            }",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

        if (partner2Name != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$partner2Name — ${
                    formatTaskRole(task.partner2Role)
                }",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = formatTaskText(
                text = task.text,
                partner1Name = partner1Name,
                partner2Name = partner2Name,
                partner1Role = task.partner1Role,
                partner2Role = task.partner2Role
            ),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        task.durationSeconds?.let { seconds ->
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onPass,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text(
                    text = stringResource(Res.string.task_pass)
                )
            }

            Button(
                onClick = onStop,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(Res.string.task_stop)
                )
            }
        }
    }
}