package com.onlysex.app.ui.screens.playercount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onlysex.app.ui.theme.OnlySexAccent
import com.onlysex.app.ui.theme.OnlySexBackground
import com.onlysex.app.ui.theme.OnlySexSecondaryText
import com.onlysex.app.ui.theme.OnlySexText
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.back
import onlysex.shared.generated.resources.player_count_description
import onlysex.shared.generated.resources.player_count_title
import onlysex.shared.generated.resources.three_players
import onlysex.shared.generated.resources.two_players
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlayerCountScreen(
    onPlayerCountSelected: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnlySexBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(
                Res.string.player_count_title
            ),
            color = OnlySexText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(
                Res.string.player_count_description
            ),
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 36.dp
            ),
            color = OnlySexSecondaryText,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

        PlayerCountButton(
            text = stringResource(
                Res.string.two_players
            ),
            onClick = {
                onPlayerCountSelected(2)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PlayerCountButton(
            text = stringResource(
                Res.string.three_players
            ),
            onClick = {
                onPlayerCountSelected(3)
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

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
    }
}

@Composable
private fun PlayerCountButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = OnlySexAccent,
            contentColor = OnlySexText
        )
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}