package com.onlysex.app.game

import com.onlysex.app.model.GameTask
import com.onlysex.app.model.Gender
import com.onlysex.app.model.Player

data class SelectedGameTask(
    val task: GameTask,
    val activePlayerIndex: Int,
    val partner1Index: Int,
    val partner2Index: Int?,
    val startsNewCycle: Boolean = false
)

object TaskSelector {

    fun select(
        tasks: List<GameTask>,
        players: List<Player>,
        activePlayerIndex: Int,
        intensityLevel: Int,
        availableToyIds: Set<String>,
        usedTaskIds: Set<String>,
        lastTaskId: String?
    ): SelectedGameTask? {
        val compatibleTasks = tasks
            .filter { task ->
                task.level == intensityLevel &&
                        task.mode == players.size &&
                        task.toyIds.all { toyId ->
                            toyId in availableToyIds
                        } &&
                        task.activeGender.matches(
                            players[activePlayerIndex].gender
                        )
            }
            .mapNotNull { task ->
                assignPlayers(
                    task = task,
                    players = players,
                    activePlayerIndex = activePlayerIndex
                )
            }

        if (compatibleTasks.isEmpty()) {
            return null
        }

        val unusedTasks = compatibleTasks.filter { selectedTask ->
            selectedTask.task.id !in usedTaskIds
        }

        if (unusedTasks.isNotEmpty()) {
            return unusedTasks
                .random()
                .copy(startsNewCycle = false)
        }

        val newCycleTasks = compatibleTasks.filter { selectedTask ->
            selectedTask.task.id != lastTaskId
        }

        val selectedTask = (
                newCycleTasks.ifEmpty {
                    compatibleTasks
                }
                )
            .random()

        return selectedTask.copy(
            startsNewCycle = true
        )
    }

    private fun assignPlayers(
        task: GameTask,
        players: List<Player>,
        activePlayerIndex: Int
    ): SelectedGameTask? {
        val otherPlayerIndices = players.indices
            .filter { index ->
                index != activePlayerIndex
            }
            .shuffled()

        if (task.participantCount == 2) {
            val partnerIndex = otherPlayerIndices.firstOrNull { index ->
                task.partner1Gender.matches(
                    players[index].gender
                )
            } ?: return null

            return SelectedGameTask(
                task = task,
                activePlayerIndex = activePlayerIndex,
                partner1Index = partnerIndex,
                partner2Index = null
            )
        }

        if (task.participantCount == 3 && players.size == 3) {
            val firstOrder = otherPlayerIndices

            if (
                task.partner1Gender.matches(
                    players[firstOrder[0]].gender
                ) &&
                task.partner2Gender.matches(
                    players[firstOrder[1]].gender
                )
            ) {
                return SelectedGameTask(
                    task = task,
                    activePlayerIndex = activePlayerIndex,
                    partner1Index = firstOrder[0],
                    partner2Index = firstOrder[1]
                )
            }

            val reversedOrder = firstOrder.reversed()

            if (
                task.partner1Gender.matches(
                    players[reversedOrder[0]].gender
                ) &&
                task.partner2Gender.matches(
                    players[reversedOrder[1]].gender
                )
            ) {
                return SelectedGameTask(
                    task = task,
                    activePlayerIndex = activePlayerIndex,
                    partner1Index = reversedOrder[0],
                    partner2Index = reversedOrder[1]
                )
            }
        }

        return null
    }

    private fun String?.matches(
        gender: Gender?
    ): Boolean {
        if (this == null || this == "any") {
            return true
        }

        return when (gender) {
            Gender.MALE -> this == "male"
            Gender.FEMALE -> this == "female"
            Gender.NON_BINARY -> false
            null -> false
        }
    }
}