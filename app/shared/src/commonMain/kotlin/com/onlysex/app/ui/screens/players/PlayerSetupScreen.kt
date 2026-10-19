package com.onlysex.app.ui.screens.players

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.onlysex.app.model.Gender
import com.onlysex.app.model.Player
import com.onlysex.app.ui.theme.OnlySexAccent
import com.onlysex.app.ui.theme.OnlySexBackground
import com.onlysex.app.ui.theme.OnlySexSecondaryText
import com.onlysex.app.ui.theme.OnlySexText
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.back
import onlysex.shared.generated.resources.continue_button
import onlysex.shared.generated.resources.gender_female
import onlysex.shared.generated.resources.gender_male
import onlysex.shared.generated.resources.gender_non_binary
import onlysex.shared.generated.resources.player_number
import onlysex.shared.generated.resources.player_setup_description
import onlysex.shared.generated.resources.player_setup_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlayerSetupScreen(
    playerCount: Int,
    onContinue: (List<Player>) -> Unit,
    onBack: () -> Unit
) {
    var selectedGenders by remember(playerCount) {
        mutableStateOf(
            List<Gender?>(playerCount) {
                null
            }
        )
    }

    val allPlayersReady = selectedGenders.all {
        it != null
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
                Res.string.player_setup_title
            ),
            color = OnlySexText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(
                Res.string.player_setup_description
            ),
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 28.dp
            ),
            color = OnlySexSecondaryText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        selectedGenders.forEachIndexed { index, selectedGender ->
            PlayerGenderSelector(
                playerNumber = index + 1,
                selectedGender = selectedGender,
                onGenderSelected = { gender ->
                    selectedGenders = selectedGenders
                        .toMutableList()
                        .also { genders ->
                            genders[index] = gender
                        }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = {
                val players = selectedGenders.mapIndexed { index, gender ->
                    Player(
                        number = index + 1,
                        gender = gender
                    )
                }

                onContinue(players)
            },
            enabled = allPlayersReady,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OnlySexAccent,
                contentColor = OnlySexText
            )
        ) {
            Text(
                text = stringResource(
                    Res.string.continue_button
                ),
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlayerGenderSelector(
    playerNumber: Int,
    selectedGender: Gender?,
    onGenderSelected: (Gender) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(
                Res.string.player_number,
                playerNumber
            ),
            modifier = Modifier.fillMaxWidth(),
            color = OnlySexAccent,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Gender.entries.forEach { gender ->
                val selected = selectedGender == gender

                val genderName = when (gender) {
                    Gender.MALE -> {
                        stringResource(
                            Res.string.gender_male
                        )
                    }

                    Gender.FEMALE -> {
                        stringResource(
                            Res.string.gender_female
                        )
                    }

                    Gender.NON_BINARY -> {
                        stringResource(
                            Res.string.gender_non_binary
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        onGenderSelected(gender)
                    },
                    modifier = Modifier.padding(
                        horizontal = 4.dp,
                        vertical = 4.dp
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 14.dp,
                        vertical = 10.dp
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selected) {
                            OnlySexAccent
                        } else {
                            OnlySexBackground
                        },
                        contentColor = OnlySexText
                    )
                ) {
                    Text(
                        text = genderName,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}