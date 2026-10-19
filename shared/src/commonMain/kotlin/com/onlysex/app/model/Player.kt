package com.onlysex.app.model

data class Player(
    val number: Int,
    val gender: Gender? = null,
    val totemId: String? = null,
    val penaltyId: String? = null
)