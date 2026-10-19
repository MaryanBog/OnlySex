package com.onlysex.app.ui.screens.game

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.onlysex.app.data.ContentDatabaseRepository
import com.onlysex.app.data.TotemRepository
import com.onlysex.app.game.GameSession
import com.onlysex.app.game.PenaltySelector
import com.onlysex.app.game.TaskSelector
import com.onlysex.app.game.TurnManager
import com.onlysex.app.localization.customAppLocale
import com.onlysex.app.model.GameTask
import com.onlysex.app.model.Penalty
import com.onlysex.app.model.Player
import com.onlysex.app.model.Totem
import com.onlysex.app.ui.screens.penalty.PenaltySelectionScreen
import onlysex.shared.generated.resources.Res
import onlysex.shared.generated.resources.loading_game
import onlysex.shared.generated.resources.no_compatible_task
import onlysex.shared.generated.resources.no_compatible_task_hint
import onlysex.shared.generated.resources.player_number
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameScreen(
    players: List<Player>,
    availableToyIds: Set<String>,
    intensityLevel: Int,
    onStop: () -> Unit
) {
    KeepScreenAwake()

    var tasks by remember(customAppLocale) {
        mutableStateOf<List<GameTask>?>(null)
    }

    var penalties by remember(customAppLocale) {
        mutableStateOf<List<Penalty>?>(null)
    }

    var totems by remember(customAppLocale) {
        mutableStateOf<List<Totem>?>(null)
    }

    var showStopDialog by remember {
        mutableStateOf(false)
    }

    var taskSelectionError by remember {
        mutableStateOf<String?>(null)
    }

    var pendingIntensityLevel by remember {
        mutableStateOf<Int?>(null)
    }

    var session by remember(
        players,
        availableToyIds,
        intensityLevel
    ) {
        mutableStateOf(
            GameSession(
                players = players,
                availableToyIds = availableToyIds,
                intensityLevel = intensityLevel,
                activePlayerIndex = TurnManager.randomFirstPlayer(
                    players.size
                )
            )
        )
    }

    LaunchedEffect(customAppLocale) {
        tasks = ContentDatabaseRepository.getTasks()
        penalties = ContentDatabaseRepository.getPenalties()
        totems = TotemRepository.getTotems()
    }

    val loadedTasks = tasks
    val loadedPenalties = penalties
    val loadedTotems = totems

    if (
        loadedTasks == null ||
        loadedPenalties == null ||
        loadedTotems == null
    ) {
        Text(
            text = stringResource(
                Res.string.loading_game
            )
        )
        return
    }

    val requestedLevel = pendingIntensityLevel

    if (requestedLevel != null) {
        PenaltySelectionScreen(
            players = session.players,
            intensityLevel = requestedLevel,
            availableToyIds = session.availableToyIds,
            onContinue = { configuredPlayers ->
                taskSelectionError = null

                session = session.copy(
                    players = configuredPlayers,
                    intensityLevel = requestedLevel,
                    currentTask = null,
                    activePenalty = null
                )

                pendingIntensityLevel = null
            },
            onBack = {
                pendingIntensityLevel = null
            }
        )

        return
    }

    val noCompatibleTaskMessage =
        stringResource(
            Res.string.no_compatible_task
        ) +
                "\n" +
                stringResource(
                    Res.string.no_compatible_task_hint
                )

    val fallbackPlayerNames = session.players.map { player ->
        stringResource(
            Res.string.player_number,
            player.number
        )
    }

    if (showStopDialog) {
        StopGameDialog(
            onConfirm = {
                showStopDialog = false
                onStop()
            },
            onDismiss = {
                showStopDialog = false
            }
        )
    }

    fun playerName(playerIndex: Int): String {
        val player = session.players[playerIndex]
        val gender = player.gender

        val totem = loadedTotems.firstOrNull { item ->
            item.id == player.totemId
        }

        return if (totem != null && gender != null) {
            totem.nameFor(gender)
        } else {
            fallbackPlayerNames[playerIndex]
        }
    }

    fun finishTurn() {
        taskSelectionError = null

        session = session.copy(
            activePlayerIndex = TurnManager.nextPlayerIndex(
                currentPlayerIndex = session.activePlayerIndex,
                playerCount = session.players.size
            ),
            currentTask = null,
            activePenalty = null
        )
    }

    val activePenalty = session.activePenalty

    if (activePenalty != null) {
        ActivePenaltyScreen(
            activePlayerName = playerName(
                activePenalty.activePlayerIndex
            ),
            recipient1Name = playerName(
                activePenalty.recipient1Index
            ),
            recipient2Name = activePenalty.recipient2Index?.let { index ->
                playerName(index)
            },
            penalty = activePenalty.penalty,
            onDone = {
                finishTurn()
            },
            onStop = {
                showStopDialog = true
            }
        )

        return
    }

    val selectedTask = session.currentTask

    if (selectedTask == null) {
        TurnScreen(
            activePlayerName = playerName(
                session.activePlayerIndex
            ),
            intensityLevel = session.intensityLevel,
            onIntensityLevelChange = { newLevel ->
                taskSelectionError = null

                if (newLevel != session.intensityLevel) {
                    pendingIntensityLevel = newLevel
                }
            },
            onRequestTask = {
                taskSelectionError = null

                val usedTaskIds =
                    session.usedTaskIdsByLevel[
                        session.intensityLevel
                    ].orEmpty()

                val newTask = TaskSelector.select(
                    tasks = loadedTasks,
                    players = session.players,
                    activePlayerIndex = session.activePlayerIndex,
                    intensityLevel = session.intensityLevel,
                    availableToyIds = session.availableToyIds,
                    usedTaskIds = usedTaskIds,
                    lastTaskId = session.lastTaskId
                )

                if (newTask != null) {
                    val nextUsedTaskIds = if (
                        newTask.startsNewCycle
                    ) {
                        setOf(newTask.task.id)
                    } else {
                        usedTaskIds + newTask.task.id
                    }

                    session = session.copy(
                        currentTask = newTask,
                        lastTaskId = newTask.task.id,
                        usedTaskIdsByLevel =
                            session.usedTaskIdsByLevel + (
                                    session.intensityLevel to
                                            nextUsedTaskIds
                                    )
                    )
                } else {
                    taskSelectionError =
                        noCompatibleTaskMessage
                }
            },
            errorMessage = taskSelectionError
        )
    } else {
        TaskScreen(
            task = selectedTask.task,
            activePlayerName = playerName(
                selectedTask.activePlayerIndex
            ),
            partner1Name = playerName(
                selectedTask.partner1Index
            ),
            partner2Name = selectedTask.partner2Index?.let { index ->
                playerName(index)
            },
            onDone = {
                finishTurn()
            },
            onPass = {
                val activePlayer =
                    session.players[
                        session.activePlayerIndex
                    ]

                val selectedPenalty =
                    loadedPenalties.firstOrNull { penalty ->
                        penalty.id == activePlayer.penaltyId
                    }

                if (selectedPenalty != null) {
                    val assignedPenalty =
                        PenaltySelector.assignRecipients(
                            penalty = selectedPenalty,
                            players = session.players,
                            activePlayerIndex =
                                session.activePlayerIndex
                        )

                    if (assignedPenalty != null) {
                        session = session.copy(
                            activePenalty = assignedPenalty
                        )
                    }
                }
            },
            onStop = {
                showStopDialog = true
            }
        )
    }
}