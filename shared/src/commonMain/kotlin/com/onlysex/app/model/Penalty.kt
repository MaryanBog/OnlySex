package com.onlysex.app.model

data class Penalty(
    val id: String,
    val intensityLevel: Int,
    val mode: Int,
    val recipientCount: Int,
    val activeGender: String,
    val partner1Gender: String,
    val partner2Gender: String?,
    val categoryId: String,
    val toyIds: Set<String>,
    val durationSeconds: Int?,
    val text: String,
    val safetyNote: String?
)