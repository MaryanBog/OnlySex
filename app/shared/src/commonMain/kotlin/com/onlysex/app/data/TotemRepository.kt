package com.onlysex.app.data

import com.onlysex.app.localization.customAppLocale
import com.onlysex.app.model.Totem

object TotemRepository {

    suspend fun getTotems(): List<Totem> {
        val useRussian = customAppLocale
            ?.lowercase()
            ?.startsWith("ru") == true

        return ContentDatabaseRepository
            .load()
            .totems
            .filter { totem ->
                totem.enabled
            }
            .sortedBy { totem ->
                totem.sortOrder
            }
            .map { totem ->
                Totem(
                    id = totem.totemId,
                    maleName = if (useRussian) {
                        totem.maleNameRu
                    } else {
                        totem.maleNameEn
                    },
                    femaleName = if (useRussian) {
                        totem.femaleNameRu
                    } else {
                        totem.femaleNameEn
                    },
                    neutralName = if (useRussian) {
                        totem.neutralNameRu
                    } else {
                        totem.neutralNameEn
                    },
                    description = if (useRussian) {
                        totem.descriptionRu
                    } else {
                        totem.descriptionEn
                    },
                    imageAsset = totem.imageAsset
                )
            }
    }
}