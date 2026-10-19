package com.onlysex.app.ui.screens.totems

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
import com.onlysex.app.data.TotemRepository
import com.onlysex.app.localization.customAppLocale
import com.onlysex.app.model.Gender
import com.onlysex.app.model.Player
import com.onlysex.app.model.Totem
import com.onlysex.app.ui.theme.OnlySexAccent
import com.onlysex.app.ui.theme.OnlySexBackground
import com.onlysex.app.ui.theme.OnlySexSecondaryText
import com.onlysex.app.ui.theme.OnlySexText
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.back
import onlysex.shared.generated.resources.continue_button
import onlysex.shared.generated.resources.next_player
import onlysex.shared.generated.resources.player_number
import onlysex.shared.generated.resources.totem_selection_description
import onlysex.shared.generated.resources.totem_selection_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun TotemSelectionScreen(
    players: List<Player>,
    onContinue: (List<Player>) -> Unit,
    onBack: () -> Unit
) {
    var totems by remember(customAppLocale) {
        mutableStateOf<List<Totem>?>(null)
    }

    LaunchedEffect(customAppLocale) {
        totems = TotemRepository.getTotems()
    }

    var currentPlayerIndex by remember {
        mutableStateOf(0)
    }

    var configuredPlayers by remember(players) {
        mutableStateOf(players)
    }

    var selectedTotemId by remember(currentPlayerIndex) {
        mutableStateOf<String?>(null)
    }

    val currentPlayer = configuredPlayers[currentPlayerIndex]
    val currentGender = currentPlayer.gender ?: Gender.NON_BINARY

    val alreadySelectedTotems = configuredPlayers
        .take(currentPlayerIndex)
        .mapNotNull { player ->
            player.totemId
        }
        .toSet()

    val availableTotems = totems
        .orEmpty()
        .filter { totem ->
            totem.id !in alreadySelectedTotems
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
                Res.string.totem_selection_title
            ),
            modifier = Modifier.padding(top = 8.dp),
            color = OnlySexText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(
                Res.string.totem_selection_description
            ),
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 24.dp
            ),
            color = OnlySexSecondaryText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        if (totems == null) {
            CircularProgressIndicator(
                color = OnlySexAccent
            )

            Spacer(modifier = Modifier.height(24.dp))
        } else {
            availableTotems.forEach { totem ->
                TotemButton(
                    totem = totem,
                    gender = currentGender,
                    selected = selectedTotemId == totem.id,
                    onClick = {
                        selectedTotemId = totem.id
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val updatedPlayer = currentPlayer.copy(
                    totemId = selectedTotemId
                )

                configuredPlayers = configuredPlayers
                    .toMutableList()
                    .also { updatedPlayers ->
                        updatedPlayers[currentPlayerIndex] = updatedPlayer
                    }

                if (currentPlayerIndex == configuredPlayers.lastIndex) {
                    onContinue(configuredPlayers)
                } else {
                    currentPlayerIndex += 1
                }
            },
            enabled = selectedTotemId != null,
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
                    stringResource(
                        Res.string.continue_button
                    )
                } else {
                    stringResource(
                        Res.string.next_player
                    )
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(
                    Res.string.back
                ),
                color = OnlySexText
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TotemButton(
    totem: Totem,
    gender: Gender,
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
                text = totem.nameFor(gender),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = totem.description,
                modifier = Modifier.padding(top = 4.dp),
                color = if (selected) {
                    OnlySexText
                } else {
                    OnlySexSecondaryText
                },
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}