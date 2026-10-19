package com.onlysex.app.model

data class Toy(
    val id: String,
    val name: String,
    val categoryId: String,
    val penetrative: Boolean,
    val description: String,
    val safetyNote: String,
    val imageAsset: String
)