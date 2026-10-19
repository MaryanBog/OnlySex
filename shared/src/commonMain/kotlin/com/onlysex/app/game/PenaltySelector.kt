package com.onlysex.app.game

import com.onlysex.app.model.Gender
import com.onlysex.app.model.Penalty
import com.onlysex.app.model.Player

data class SelectedPenalty(
    val penalty: Penalty,
    val activePlayerIndex: Int,
    val recipient1Index: Int,
    val recipient2Index: Int?
)

object PenaltySelector {

    fun selectOptions(
        penalties: List<Penalty>,
        players: List<Player>,
        activePlayerIndex: Int,
        intensityLevel: Int,
        availableToyIds: Set<String>,
        optionCount: Int = 3
    ): List<Penalty> {
        val activePlayer = players[activePlayerIndex]

        val compatiblePenalties = penalties.filter { penalty ->
            penalty.mode == players.size &&
                    penalty.intensityLevel <= intensityLevel &&
                    penalty.activeGender.matches(activePlayer.gender) &&
                    penalty.toyIds.all { toyId ->
                        toyId in availableToyIds
                    } &&
                    assignRecipients(
                        penalty = penalty,
                        players = players,
                        activePlayerIndex = activePlayerIndex
                    ) != null
        }

        if (compatiblePenalties.isEmpty()) {
            return emptyList()
        }

        val optionsFromDifferentCategories = compatiblePenalties
            .groupBy { penalty ->
                penalty.categoryId
            }
            .values
            .shuffled()
            .map { categoryPenalties ->
                categoryPenalties.random()
            }
            .take(optionCount)

        if (optionsFromDifferentCategories.size >= optionCount) {
            return optionsFromDifferentCategories
        }

        val selectedIds = optionsFromDifferentCategories
            .map { penalty ->
                penalty.id
            }
            .toSet()

        val additionalOptions = compatiblePenalties
            .filter { penalty ->
                penalty.id !in selectedIds
            }
            .shuffled()
            .take(optionCount - optionsFromDifferentCategories.size)

        return optionsFromDifferentCategories + additionalOptions
    }

    fun assignRecipients(
        penalty: Penalty,
        players: List<Player>,
        activePlayerIndex: Int
    ): SelectedPenalty? {
        val otherPlayerIndices = players.indices
            .filter { index ->
                index != activePlayerIndex
            }
            .shuffled()

        if (penalty.recipientCount == 1) {
            val recipientIndex = otherPlayerIndices.firstOrNull { index ->
                penalty.partner1Gender.matches(players[index].gender)
            } ?: return null

            return SelectedPenalty(
                penalty = penalty,
                activePlayerIndex = activePlayerIndex,
                recipient1Index = recipientIndex,
                recipient2Index = null
            )
        }

        if (
            penalty.recipientCount == 2 &&
            players.size == 3 &&
            otherPlayerIndices.size == 2
        ) {
            val firstOrder = otherPlayerIndices

            if (
                penalty.partner1Gender.matches(
                    players[firstOrder[0]].gender
                ) &&
                penalty.partner2Gender.matches(
                    players[firstOrder[1]].gender
                )
            ) {
                return SelectedPenalty(
                    penalty = penalty,
                    activePlayerIndex = activePlayerIndex,
                    recipient1Index = firstOrder[0],
                    recipient2Index = firstOrder[1]
                )
            }

            val reversedOrder = firstOrder.reversed()

            if (
                penalty.partner1Gender.matches(
                    players[reversedOrder[0]].gender
                ) &&
                penalty.partner2Gender.matches(
                    players[reversedOrder[1]].gender
                )
            ) {
                return SelectedPenalty(
                    penalty = penalty,
                    activePlayerIndex = activePlayerIndex,
                    recipient1Index = reversedOrder[0],
                    recipient2Index = reversedOrder[1]
                )
            }
        }

        return null
    }

    private fun String?.matches(gender: Gender?): Boolean {
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