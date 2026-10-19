package com.onlysex.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.onlysex.app.model.Player
import com.onlysex.app.localization.AppLanguage
import com.onlysex.app.ui.screens.game.GameScreen
import com.onlysex.app.ui.screens.game.GameOverScreen
import com.onlysex.app.ui.screens.intensity.IntensityScreen
import com.onlysex.app.ui.screens.penalty.PenaltySelectionScreen
import com.onlysex.app.ui.screens.playercount.PlayerCountScreen
import com.onlysex.app.ui.screens.players.PlayerSetupScreen
import com.onlysex.app.ui.screens.summary.SetupSummaryScreen
import com.onlysex.app.ui.screens.totems.TotemSelectionScreen
import com.onlysex.app.ui.screens.toys.ToySelectionScreen
import com.onlysex.app.ui.screens.welcome.WelcomeScreen
import androidx.compose.runtime.CompositionLocalProvider
import com.onlysex.app.localization.LocalAppLanguage
import com.onlysex.app.localization.LocalAppLocale
import com.onlysex.app.localization.customAppLocale
import androidx.compose.runtime.LaunchedEffect

private enum class AppScreen {
    WELCOME,
    PLAYER_COUNT,
    PLAYER_SETUP,
    TOTEM_SELECTION,
    TOY_SELECTION,
    INTENSITY,
    PENALTY_SELECTION,
    SETUP_SUMMARY,
    GAME,
    GAME_OVER
}

@Composable
fun AppNavigation() {
    var currentScreen by remember {
        mutableStateOf(AppScreen.WELCOME)
    }

    val currentLanguageCode =
        customAppLocale
            ?: LocalAppLocale.current.substringBefore('-')

    val selectedLanguage =
        AppLanguage.entries.firstOrNull { language ->
            language.code == currentLanguageCode
        } ?: AppLanguage.ENGLISH

    LaunchedEffect(selectedLanguage) {
        if (customAppLocale == null) {
            customAppLocale = selectedLanguage.code
        }
    }

    var selectedPlayerCount by remember {
        mutableStateOf(2)
    }

    var players by remember {
        mutableStateOf<List<Player>>(emptyList())
    }

    var selectedToyIds by remember {
        mutableStateOf<Set<String>>(emptySet())
    }

    var selectedIntensityLevel by remember {
        mutableStateOf(1)
    }

    CompositionLocalProvider(
        LocalAppLanguage provides selectedLanguage
    ) {
        when (currentScreen) {
        AppScreen.WELCOME -> {
            WelcomeScreen(
                selectedLanguage = selectedLanguage,
                onLanguageChange = { language ->
                    customAppLocale = language.code
                },
                onStartGame = {
                    currentScreen = AppScreen.PLAYER_COUNT
                }
            )
        }

        AppScreen.PLAYER_COUNT -> {
            PlayerCountScreen(
                onPlayerCountSelected = { playerCount ->
                    selectedPlayerCount = playerCount
                    currentScreen = AppScreen.PLAYER_SETUP
                },
                onBack = {
                    currentScreen = AppScreen.WELCOME
                }
            )
        }

        AppScreen.PLAYER_SETUP -> {
            PlayerSetupScreen(
                playerCount = selectedPlayerCount,
                onContinue = { configuredPlayers ->
                    players = configuredPlayers
                    currentScreen = AppScreen.TOTEM_SELECTION
                },
                onBack = {
                    currentScreen = AppScreen.PLAYER_COUNT
                }
            )
        }

        AppScreen.TOTEM_SELECTION -> {
            TotemSelectionScreen(
                players = players,
                onContinue = { configuredPlayers ->
                    players = configuredPlayers
                    currentScreen = AppScreen.TOY_SELECTION
                },
                onBack = {
                    currentScreen = AppScreen.PLAYER_SETUP
                }
            )
        }

        AppScreen.TOY_SELECTION -> {
            ToySelectionScreen(
                onContinue = { toyIds ->
                    selectedToyIds = toyIds
                    currentScreen = AppScreen.INTENSITY
                },
                onBack = {
                    currentScreen = AppScreen.TOTEM_SELECTION
                }
            )
        }

        AppScreen.INTENSITY -> {
            IntensityScreen(
                onContinue = { level ->
                    selectedIntensityLevel = level
                    currentScreen = AppScreen.PENALTY_SELECTION
                },
                onBack = {
                    currentScreen = AppScreen.TOY_SELECTION
                }
            )
        }

        AppScreen.PENALTY_SELECTION -> {
            PenaltySelectionScreen(
                players = players,
                intensityLevel = selectedIntensityLevel,
                availableToyIds = selectedToyIds,
                onContinue = { configuredPlayers ->
                    players = configuredPlayers
                    currentScreen = AppScreen.SETUP_SUMMARY
                },
                onBack = {
                    currentScreen = AppScreen.INTENSITY
                }
            )
        }

        AppScreen.SETUP_SUMMARY -> {
            SetupSummaryScreen(
                players = players,
                intensityLevel = selectedIntensityLevel,
                selectedToyIds = selectedToyIds,
                onStartGame = {
                    currentScreen = AppScreen.GAME
                },
                onBack = {
                    currentScreen = AppScreen.PENALTY_SELECTION
                }
            )
        }

        AppScreen.GAME -> {
            GameScreen(
                players = players,
                availableToyIds = selectedToyIds,
                intensityLevel = selectedIntensityLevel,
                onStop = {
                    currentScreen = AppScreen.GAME_OVER
                }
            )
        }

        AppScreen.GAME_OVER -> {
            GameOverScreen(
                onEndSession = {
                    players = emptyList()
                    selectedToyIds = emptySet()
                    selectedIntensityLevel = 1
                    selectedPlayerCount = 2
                    currentScreen = AppScreen.WELCOME
                }
            )
        }
        }
    }
}