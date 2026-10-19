package com.onlysex.app.game

import com.onlysex.app.model.Player

data class GameSession(
    val players: List<Player>,
    val availableToyIds: Set<String>,
    val intensityLevel: Int,
    val activePlayerIndex: Int,
    val usedTaskIdsByLevel: Map<Int, Set<String>> = emptyMap(),
    val lastTaskId: String? = null,
    val currentTask: SelectedGameTask? = null,
    val activePenalty: SelectedPenalty? = null
)