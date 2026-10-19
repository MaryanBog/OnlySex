package com.onlysex.app.ui.screens.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.onlysex.app.data.ToyRepository
import com.onlysex.app.localization.customAppLocale
import com.onlysex.app.model.Gender
import com.onlysex.app.model.Player
import com.onlysex.app.model.Totem
import com.onlysex.app.model.Toy
import com.onlysex.app.ui.theme.OnlySexAccent
import com.onlysex.app.ui.theme.OnlySexBackground
import com.onlysex.app.ui.theme.OnlySexSecondaryText
import com.onlysex.app.ui.theme.OnlySexText
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.back
import onlysex.shared.generated.resources.gender_female
import onlysex.shared.generated.resources.gender_male
import onlysex.shared.generated.resources.gender_non_binary
import onlysex.shared.generated.resources.player_number
import onlysex.shared.generated.resources.start_game
import onlysex.shared.generated.resources.summary_available_toys
import onlysex.shared.generated.resources.summary_description
import onlysex.shared.generated.resources.summary_intensity
import onlysex.shared.generated.resources.summary_level
import onlysex.shared.generated.resources.summary_no_totem
import onlysex.shared.generated.resources.summary_no_toys
import onlysex.shared.generated.resources.summary_penalty_private
import onlysex.shared.generated.resources.summary_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetupSummaryScreen(
    players: List<Player>,
    intensityLevel: Int,
    selectedToyIds: Set<String>,
    onStartGame: () -> Unit,
    onBack: () -> Unit
) {
    var totems by remember(customAppLocale) {
        mutableStateOf<List<Totem>?>(null)
    }

    var toys by remember(customAppLocale) {
        mutableStateOf<List<Toy>?>(null)
    }

    LaunchedEffect(customAppLocale) {
        totems = TotemRepository.getTotems()
        toys = ToyRepository.getToys()
    }

    val contentLoaded = totems != null && toys != null

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
                Res.string.summary_title
            ),
            color = OnlySexText,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(
                Res.string.summary_description
            ),
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 28.dp
            ),
            color = OnlySexSecondaryText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        if (!contentLoaded) {
            CircularProgressIndicator(
                color = OnlySexAccent
            )

            Spacer(modifier = Modifier.height(24.dp))
        } else {
            players.forEach { player ->
                PlayerSummary(
                    player = player,
                    totems = totems.orEmpty()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            SummarySection(
                title = stringResource(
                    Res.string.summary_intensity
                ),
                value = stringResource(
                    Res.string.summary_level,
                    intensityLevel
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            val selectedToyNames = toys
                .orEmpty()
                .filter { toy ->
                    toy.id in selectedToyIds
                }
                .joinToString { toy ->
                    toy.name
                }

            SummarySection(
                title = stringResource(
                    Res.string.summary_available_toys
                ),
                value = selectedToyNames.ifBlank {
                    stringResource(
                        Res.string.summary_no_toys
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStartGame,
            enabled = contentLoaded,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OnlySexAccent,
                contentColor = OnlySexText
            )
        ) {
            Text(
                text = stringResource(
                    Res.string.start_game
                ),
                fontSize = 19.sp,
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
private fun PlayerSummary(
    player: Player,
    totems: List<Totem>
) {
    val gender = player.gender ?: Gender.NON_BINARY

    val totem = totems.firstOrNull { item ->
        item.id == player.totemId
    }

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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                Res.string.player_number,
                player.number
            ),
            color = OnlySexAccent,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = totem?.nameFor(gender)
                ?: stringResource(
                    Res.string.summary_no_totem
                ),
            modifier = Modifier.padding(top = 6.dp),
            color = OnlySexText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = genderName,
            modifier = Modifier.padding(top = 4.dp),
            color = OnlySexSecondaryText,
            fontSize = 15.sp
        )

        Text(
            text = stringResource(
                Res.string.summary_penalty_private
            ),
            modifier = Modifier.padding(top = 8.dp),
            color = OnlySexSecondaryText,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun SummarySection(
    title: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = OnlySexAccent,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            modifier = Modifier.padding(top = 6.dp),
            color = OnlySexText,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )
    }
}