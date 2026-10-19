package com.onlysex.app.ui.screens.intensity

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
import com.onlysex.app.ui.theme.OnlySexAccent
import com.onlysex.app.ui.theme.OnlySexBackground
import com.onlysex.app.ui.theme.OnlySexSecondaryText
import com.onlysex.app.ui.theme.OnlySexText
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.back
import onlysex.shared.generated.resources.continue_button
import onlysex.shared.generated.resources.intensity_1_description
import onlysex.shared.generated.resources.intensity_1_name
import onlysex.shared.generated.resources.intensity_2_description
import onlysex.shared.generated.resources.intensity_2_name
import onlysex.shared.generated.resources.intensity_3_description
import onlysex.shared.generated.resources.intensity_3_name
import onlysex.shared.generated.resources.intensity_4_description
import onlysex.shared.generated.resources.intensity_4_name
import onlysex.shared.generated.resources.intensity_5_description
import onlysex.shared.generated.resources.intensity_5_name
import onlysex.shared.generated.resources.intensity_description
import onlysex.shared.generated.resources.intensity_level
import onlysex.shared.generated.resources.intensity_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private data class IntensityOption(
    val level: Int,
    val nameResource: StringResource,
    val descriptionResource: StringResource
)

private val intensityOptions = listOf(
    IntensityOption(
        level = 1,
        nameResource = Res.string.intensity_1_name,
        descriptionResource = Res.string.intensity_1_description
    ),
    IntensityOption(
        level = 2,
        nameResource = Res.string.intensity_2_name,
        descriptionResource = Res.string.intensity_2_description
    ),
    IntensityOption(
        level = 3,
        nameResource = Res.string.intensity_3_name,
        descriptionResource = Res.string.intensity_3_description
    ),
    IntensityOption(
        level = 4,
        nameResource = Res.string.intensity_4_name,
        descriptionResource = Res.string.intensity_4_description
    ),
    IntensityOption(
        level = 5,
        nameResource = Res.string.intensity_5_name,
        descriptionResource = Res.string.intensity_5_description
    )
)

@Composable
fun IntensityScreen(
    onContinue: (Int) -> Unit,
    onBack: () -> Unit
) {
    var selectedLevel by remember {
        mutableStateOf<Int?>(null)
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
            text = stringResource(Res.string.intensity_title),
            color = OnlySexText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(Res.string.intensity_description),
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 24.dp
            ),
            color = OnlySexSecondaryText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        intensityOptions.forEach { option ->
            IntensityButton(
                option = option,
                selected = selectedLevel == option.level,
                onClick = {
                    selectedLevel = option.level
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                selectedLevel?.let(onContinue)
            },
            enabled = selectedLevel != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OnlySexAccent,
                contentColor = OnlySexText
            )
        ) {
            Text(
                text = stringResource(Res.string.continue_button),
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
                text = stringResource(Res.string.back),
                color = OnlySexText
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun IntensityButton(
    option: IntensityOption,
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
                text = stringResource(
                    Res.string.intensity_level,
                    option.level
                ),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(option.nameResource),
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(option.descriptionResource),
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