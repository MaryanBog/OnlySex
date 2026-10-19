package com.onlysex.app.game

import kotlin.random.Random

object TurnManager {

    fun randomFirstPlayer(playerCount: Int): Int {
        require(playerCount in 2..3)
        return Random.nextInt(playerCount)
    }

    fun nextPlayerIndex(
        currentPlayerIndex: Int,
        playerCount: Int
    ): Int {
        require(playerCount in 2..3)
        return (currentPlayerIndex + 1) % playerCount
    }
}