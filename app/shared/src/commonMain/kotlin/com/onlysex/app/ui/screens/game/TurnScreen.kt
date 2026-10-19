package com.onlysex.app.ui.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onlysex.app.sensor.ShakeDetector
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.get_task
import onlysex.shared.generated.resources.turn_intensity_level
import onlysex.shared.generated.resources.turn_shake_instruction
import onlysex.shared.generated.resources.turn_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun TurnScreen(
    activePlayerName: String,
    intensityLevel: Int,
    onIntensityLevelChange: (Int) -> Unit,
    onRequestTask: () -> Unit,
    errorMessage: String? = null
) {
    ShakeDetector(
        enabled = true,
        onShake = onRequestTask
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.turn_title),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = activePlayerName,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(
                    Res.string.turn_intensity_level
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (1..5).forEach { level ->
                    if (level == intensityLevel) {
                        Button(
                            onClick = {
                                onIntensityLevelChange(level)
                            },
                            modifier = Modifier.weight(1f),
                            contentPadding = ButtonDefaults.ContentPadding
                        ) {
                            Text(
                                text = level.toString(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                onIntensityLevelChange(level)
                            },
                            modifier = Modifier.weight(1f),
                            contentPadding = ButtonDefaults.ContentPadding
                        ) {
                            Text(text = level.toString())
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(
                    Res.string.turn_shake_instruction
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onRequestTask,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(Res.string.get_task),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}