package com.onlysex.app.data

import com.onlysex.app.localization.customAppLocale
import com.onlysex.app.model.Toy

object ToyRepository {

    suspend fun getToys(): List<Toy> {
        val useRussian = customAppLocale
            ?.lowercase()
            ?.startsWith("ru") == true

        return ContentDatabaseRepository
            .load()
            .toys
            .filter { toy ->
                toy.enabled
            }
            .map { toy ->
                Toy(
                    id = toy.toyId,
                    name = if (useRussian) {
                        toy.nameRu
                    } else {
                        toy.nameEn
                    },
                    categoryId = toy.categoryId,
                    penetrative = toy.penetrative,
                    description = if (useRussian) {
                        toy.descriptionRu
                    } else {
                        toy.descriptionEn
                    },
                    safetyNote = if (useRussian) {
                        toy.safetyNoteRu.orEmpty()
                    } else {
                        toy.safetyNoteEn.orEmpty()
                    },
                    imageAsset = toy.imageAsset
                )
            }
    }
}