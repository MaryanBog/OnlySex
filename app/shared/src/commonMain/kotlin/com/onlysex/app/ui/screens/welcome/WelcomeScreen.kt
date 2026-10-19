package com.onlysex.app.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.onlysex.app.localization.AppLanguage
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.start_game
import onlysex.shared.generated.resources.welcome_tagline
import org.jetbrains.compose.resources.stringResource
import com.onlysex.app.ui.theme.OnlySexAccent
import com.onlysex.app.ui.theme.OnlySexBackground
import com.onlysex.app.ui.theme.OnlySexText

@Composable
fun WelcomeScreen(
    onStartGame: () -> Unit,
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    onLanguageChange: (AppLanguage) -> Unit = {}
) {
    var languageMenuExpanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OnlySexBackground)
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            OutlinedButton(
                onClick = {
                    languageMenuExpanded = true
                }
            ) {
                Text(
                    text = "🌐 ${selectedLanguage.code.uppercase()}",
                    color = OnlySexText,
                    fontWeight = FontWeight.Bold
                )
            }

            DropdownMenu(
                expanded = languageMenuExpanded,
                onDismissRequest = {
                    languageMenuExpanded = false
                }
            ) {
                AppLanguage.entries.forEach { language ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = language.displayName,
                                fontWeight = if (language == selectedLanguage) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        },
                        onClick = {
                            languageMenuExpanded = false
                            onLanguageChange(language)
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ONLYSEX",
                color = OnlySexAccent,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(Res.string.welcome_tagline),
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 40.dp
                ),
                color = OnlySexText,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onStartGame,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OnlySexAccent,
                    contentColor = OnlySexText
                )
            ) {
                Text(
                    text = stringResource(Res.string.start_game),
                    modifier = Modifier.padding(8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}