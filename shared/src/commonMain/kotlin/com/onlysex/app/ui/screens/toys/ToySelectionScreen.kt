package com.onlysex.app.ui.screens.toys

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
import com.onlysex.app.data.ToyRepository
import com.onlysex.app.localization.customAppLocale
import com.onlysex.app.model.Toy
import com.onlysex.app.ui.theme.OnlySexAccent
import com.onlysex.app.ui.theme.OnlySexBackground
import com.onlysex.app.ui.theme.OnlySexSecondaryText
import com.onlysex.app.ui.theme.OnlySexText
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.back
import onlysex.shared.generated.resources.continue_button
import onlysex.shared.generated.resources.toy_selection_description
import onlysex.shared.generated.resources.toy_selection_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ToySelectionScreen(
    onContinue: (Set<String>) -> Unit,
    onBack: () -> Unit
) {
    var toys by remember(customAppLocale) {
        mutableStateOf<List<Toy>?>(null)
    }

    LaunchedEffect(customAppLocale) {
        toys = ToyRepository.getToys()
    }

    var selectedToyIds by remember {
        mutableStateOf<Set<String>>(emptySet())
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
                Res.string.toy_selection_title
            ),
            color = OnlySexText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(
                Res.string.toy_selection_description
            ),
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 24.dp
            ),
            color = OnlySexSecondaryText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        if (toys == null) {
            CircularProgressIndicator(
                color = OnlySexAccent
            )

            Spacer(modifier = Modifier.height(24.dp))
        } else {
            toys.orEmpty().forEach { toy ->
                ToyButton(
                    toy = toy,
                    selected = toy.id in selectedToyIds,
                    onClick = {
                        selectedToyIds = if (
                            toy.id in selectedToyIds
                        ) {
                            selectedToyIds - toy.id
                        } else {
                            selectedToyIds + toy.id
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onContinue(selectedToyIds)
            },
            enabled = toys != null,
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

@Composable
private fun ToyButton(
    toy: Toy,
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
                text = if (selected) {
                    "✓ ${toy.name}"
                } else {
                    toy.name
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = toy.description,
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