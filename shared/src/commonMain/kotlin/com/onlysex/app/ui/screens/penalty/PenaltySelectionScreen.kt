package com.onlysex.app.ui.screens.penalty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.onlysex.app.data.ContentDatabaseRepository
import com.onlysex.app.game.PenaltySelector
import com.onlysex.app.model.Penalty
import com.onlysex.app.model.Player
import com.onlysex.app.ui.theme.OnlySexAccent
import com.onlysex.app.ui.theme.OnlySexBackground
import com.onlysex.app.ui.theme.OnlySexSecondaryText
import com.onlysex.app.ui.theme.OnlySexText
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.back
import onlysex.shared.generated.resources.confirm_and_pass_phone
import onlysex.shared.generated.resources.continue_button
import onlysex.shared.generated.resources.database_error
import onlysex.shared.generated.resources.duration_seconds
import onlysex.shared.generated.resources.no_compatible_penalties
import onlysex.shared.generated.resources.penalty_selection_description
import onlysex.shared.generated.resources.penalty_selection_title
import onlysex.shared.generated.resources.player_number
import onlysex.shared.generated.resources.unable_to_load_penalties
import org.jetbrains.compose.resources.stringResource

@Composable
fun PenaltySelectionScreen(
    players: List<Player>,
    intensityLevel: Int,
    availableToyIds: Set<String>,
    onContinue: (List<Player>) -> Unit,
    onBack: () -> Unit
) {
    var penalties by remember {
        mutableStateOf<List<Penalty>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val unableToLoadMessage = stringResource(
        Res.string.unable_to_load_penalties
    )

    LaunchedEffect(Unit) {
        try {
            penalties = ContentDatabaseRepository.getPenalties()
        } catch (error: Throwable) {
            errorMessage = error.message ?: unableToLoadMessage
        } finally {
            loading = false
        }
    }

    if (loading) {
        LoadingScreen()
        return
    }

    if (errorMessage != null) {
        ErrorScreen(
            message = errorMessage.orEmpty(),
            onBack = onBack
        )
        return
    }

    var currentPlayerIndex by remember {
        mutableStateOf(0)
    }

    var configuredPlayers by remember(players) {
        mutableStateOf(players)
    }

    var selectedPenaltyId by remember(currentPlayerIndex) {
        mutableStateOf<String?>(null)
    }

    val currentPlayer = configuredPlayers[currentPlayerIndex]

    val penaltyOptions = remember(
        currentPlayerIndex,
        penalties,
        intensityLevel,
        availableToyIds
    ) {
        PenaltySelector.selectOptions(
            penalties = penalties,
            players = configuredPlayers,
            activePlayerIndex = currentPlayerIndex,
            intensityLevel = intensityLevel,
            availableToyIds = availableToyIds
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnlySexBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(
                Res.string.player_number,
                currentPlayer.number
            ),
            color = OnlySexAccent,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(
                Res.string.penalty_selection_title
            ),
            modifier = Modifier.padding(top = 8.dp),
            color = OnlySexText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(
                Res.string.penalty_selection_description
            ),
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 24.dp
            ),
            color = OnlySexSecondaryText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        penaltyOptions.forEach { penalty ->
            PenaltyButton(
                penalty = penalty,
                selected = selectedPenaltyId == penalty.id,
                onClick = {
                    selectedPenaltyId = penalty.id
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (penaltyOptions.isEmpty()) {
            Text(
                text = stringResource(
                    Res.string.no_compatible_penalties
                ),
                color = OnlySexText,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val updatedPlayers = configuredPlayers
                    .toMutableList()
                    .also { playersList ->
                        playersList[currentPlayerIndex] =
                            currentPlayer.copy(
                                penaltyId = selectedPenaltyId
                            )
                    }

                configuredPlayers = updatedPlayers

                if (currentPlayerIndex == updatedPlayers.lastIndex) {
                    onContinue(updatedPlayers)
                } else {
                    currentPlayerIndex += 1
                }
            },
            enabled = selectedPenaltyId != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OnlySexAccent,
                contentColor = OnlySexText
            )
        ) {
            Text(
                text = if (
                    currentPlayerIndex == configuredPlayers.lastIndex
                ) {
                    stringResource(Res.string.continue_button)
                } else {
                    stringResource(
                        Res.string.confirm_and_pass_phone
                    )
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.back),
                color = OnlySexText
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PenaltyButton(
    penalty: Penalty,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) {
                OnlySexAccent
            } else {
                OnlySexBackground
            },
            contentColor = OnlySexText
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = penalty.text,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            penalty.durationSeconds?.let { seconds ->
                Text(
                    text = stringResource(
                        Res.string.duration_seconds,
                        seconds
                    ),
                    modifier = Modifier.padding(top = 6.dp),
                    color = if (selected) {
                        OnlySexText
                    } else {
                        OnlySexSecondaryText
                    },
                    fontSize = 14.sp
                )
            }

            penalty.safetyNote?.let { safetyNote ->
                Text(
                    text = safetyNote,
                    modifier = Modifier.padding(top = 6.dp),
                    color = if (selected) {
                        OnlySexText
                    } else {
                        OnlySexSecondaryText
                    },
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnlySexBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(200.dp))

        CircularProgressIndicator(
            color = OnlySexAccent
        )
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnlySexBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(160.dp))

        Text(
            text = stringResource(Res.string.database_error),
            color = OnlySexAccent,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = message,
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 24.dp
            ),
            color = OnlySexText,
            textAlign = TextAlign.Center
        )

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.back),
                color = OnlySexText
            )
        }
    }
}