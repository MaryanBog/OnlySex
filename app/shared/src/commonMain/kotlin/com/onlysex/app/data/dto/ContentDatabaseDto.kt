package com.onlysex.app.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentDatabaseDto(
    val tasks: List<TaskDto>,
    val penalties: List<PenaltyDto>,
    val totems: List<TotemDto>,
    val toys: List<ToyDto>,
    val categories: List<CategoryDto>
)

@Serializable
data class TaskDto(
    @SerialName("task_id")
    val taskId: String,

    val level: Int,
    val mode: Int,

    @SerialName("scenario_type")
    val scenarioType: String,

    @SerialName("participant_count")
    val participantCount: Int,

    @SerialName("active_role")
    val activeRole: String,

    @SerialName("partner_1_role")
    val partner1Role: String,

    @SerialName("partner_2_role")
    val partner2Role: String? = null,

    @SerialName("active_gender")
    val activeGender: String,

    @SerialName("partner_1_gender")
    val partner1Gender: String,

    @SerialName("partner_2_gender")
    val partner2Gender: String? = null,

    @SerialName("category_id")
    val categoryId: String,

    @SerialName("toy_ids")
    val toyIds: List<String>? = null,

    @SerialName("duration_seconds")
    val durationSeconds: Int? = null,

    @SerialName("orgasm_allowed")
    val orgasmAllowed: Boolean,

    @SerialName("text_en")
    val textEn: String,

    @SerialName("text_ru")
    val textRu: String,

    @SerialName("safety_note")
    val safetyNoteEn: String? = null,

    @SerialName("safety_note_ru")
    val safetyNoteRu: String? = null,

    val enabled: Boolean
)

@Serializable
data class PenaltyDto(
    @SerialName("penalty_id")
    val penaltyId: String,

    @SerialName("intensity_level")
    val intensityLevel: Int,

    val mode: Int,

    @SerialName("recipient_count")
    val recipientCount: Int,

    @SerialName("active_gender")
    val activeGender: String,

    @SerialName("partner_1_gender")
    val partner1Gender: String,

    @SerialName("partner_2_gender")
    val partner2Gender: String? = null,

    @SerialName("category_id")
    val categoryId: String,

    @SerialName("toy_ids")
    val toyIds: List<String>? = null,

    @SerialName("duration_seconds")
    val durationSeconds: Int? = null,

    @SerialName("text_en")
    val textEn: String,

    @SerialName("text_ru")
    val textRu: String,

    @SerialName("safety_note")
    val safetyNoteEn: String? = null,

    @SerialName("safety_note_ru")
    val safetyNoteRu: String? = null,

    val enabled: Boolean
)

@Serializable
data class TotemDto(
    @SerialName("totem_id")
    val totemId: String,

    @SerialName("name_male_en")
    val maleNameEn: String,

    @SerialName("name_female_en")
    val femaleNameEn: String,

    @SerialName("name_neutral_en")
    val neutralNameEn: String,

    @SerialName("description_en")
    val descriptionEn: String,

    @SerialName("name_male_ru")
    val maleNameRu: String,

    @SerialName("name_female_ru")
    val femaleNameRu: String,

    @SerialName("name_neutral_ru")
    val neutralNameRu: String,

    @SerialName("description_ru")
    val descriptionRu: String,

    @SerialName("image_asset")
    val imageAsset: String,

    @SerialName("sort_order")
    val sortOrder: Int,

    val enabled: Boolean
)

@Serializable
data class ToyDto(
    @SerialName("toy_id")
    val toyId: String,

    @SerialName("name_en")
    val nameEn: String,

    @SerialName("category_id")
    val categoryId: String,

    val penetrative: Boolean,

    @SerialName("description_en")
    val descriptionEn: String,

    @SerialName("safety_note")
    val safetyNoteEn: String? = null,

    @SerialName("name_ru")
    val nameRu: String,

    @SerialName("description_ru")
    val descriptionRu: String,

    @SerialName("safety_note_ru")
    val safetyNoteRu: String? = null,

    @SerialName("image_asset")
    val imageAsset: String,

    val enabled: Boolean
)

@Serializable
data class CategoryDto(
    @SerialName("category_id")
    val categoryId: String,

    @SerialName("name_en")
    val nameEn: String,

    @SerialName("content_type")
    val contentType: String,

    @SerialName("description_en")
    val descriptionEn: String,

    @SerialName("name_ru")
    val nameRu: String,

    @SerialName("description_ru")
    val descriptionRu: String,

    val enabled: Boolean
)