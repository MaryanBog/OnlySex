package com.onlysex.app.data

import com.onlysex.app.data.dto.ContentDatabaseDto
import com.onlysex.app.localization.customAppLocale
import com.onlysex.app.model.GameTask
import com.onlysex.app.model.Penalty
import kotlinx.serialization.json.Json
import onlysex.shared.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

object ContentDatabaseRepository {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private var cachedDatabase: ContentDatabaseDto? = null

    private fun isRussianLanguage(): Boolean {
        return customAppLocale
            ?.lowercase()
            ?.startsWith("ru") == true
    }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun load(): ContentDatabaseDto {
        cachedDatabase?.let { database ->
            return database
        }

        val bytes = Res.readBytes(
            path = "files/content.json"
        )

        val database = json.decodeFromString<ContentDatabaseDto>(
            bytes.decodeToString()
        )

        cachedDatabase = database

        return database
    }

    suspend fun getPenalties(): List<Penalty> {
        val useRussian = isRussianLanguage()

        return load()
            .penalties
            .filter { penalty ->
                penalty.enabled
            }
            .map { penalty ->
                Penalty(
                    id = penalty.penaltyId,
                    intensityLevel = penalty.intensityLevel,
                    mode = penalty.mode,
                    recipientCount = penalty.recipientCount,
                    activeGender = penalty.activeGender,
                    partner1Gender = penalty.partner1Gender,
                    partner2Gender = penalty.partner2Gender,
                    categoryId = penalty.categoryId,
                    toyIds = penalty.toyIds
                        .orEmpty()
                        .toSet(),
                    durationSeconds = penalty.durationSeconds,
                    text = if (useRussian) {
                        penalty.textRu
                    } else {
                        penalty.textEn
                    },
                    safetyNote = if (useRussian) {
                        penalty.safetyNoteRu
                    } else {
                        penalty.safetyNoteEn
                    }
                )
            }
    }

    suspend fun getTasks(): List<GameTask> {
        val useRussian = isRussianLanguage()

        return load()
            .tasks
            .filter { task ->
                task.enabled
            }
            .map { task ->
                GameTask(
                    id = task.taskId,
                    level = task.level,
                    mode = task.mode,
                    scenarioType = task.scenarioType,
                    participantCount = task.participantCount,
                    activeRole = task.activeRole,
                    partner1Role = task.partner1Role,
                    partner2Role = task.partner2Role,
                    activeGender = task.activeGender,
                    partner1Gender = task.partner1Gender,
                    partner2Gender = task.partner2Gender,
                    categoryId = task.categoryId,
                    toyIds = task.toyIds
                        .orEmpty()
                        .toSet(),
                    durationSeconds = task.durationSeconds,
                    orgasmAllowed = task.orgasmAllowed,
                    text = if (useRussian) {
                        task.textRu
                    } else {
                        task.textEn
                    },
                    safetyNote = if (useRussian) {
                        task.safetyNoteRu
                    } else {
                        task.safetyNoteEn
                    }
                )
            }
    }

    fun clearCache() {
        cachedDatabase = null
    }
}