package com.onlysex.app.model

data class Totem(
    val id: String,
    val maleName: String,
    val femaleName: String,
    val neutralName: String,
    val description: String,
    val imageAsset: String
) {
    fun nameFor(gender: Gender): String {
        return when (gender) {
            Gender.MALE -> maleName
            Gender.FEMALE -> femaleName
            Gender.NON_BINARY -> neutralName
        }
    }
}