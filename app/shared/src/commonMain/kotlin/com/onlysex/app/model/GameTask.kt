package com.onlysex.app.model

data class GameTask(
    val id: String,
    val level: Int,
    val mode: Int,
    val scenarioType: String,
    val participantCount: Int,
    val activeRole: String,
    val partner1Role: String,
    val partner2Role: String?,
    val activeGender: String,
    val partner1Gender: String,
    val partner2Gender: String?,
    val categoryId: String,
    val toyIds: Set<String>,
    val durationSeconds: Int?,
    val orgasmAllowed: Boolean,
    val text: String,
    val safetyNote: String?
)