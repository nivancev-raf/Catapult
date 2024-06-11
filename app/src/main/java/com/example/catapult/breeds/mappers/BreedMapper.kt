package com.example.catapult.breeds.mappers

import com.example.catapult.breeds.api.model.BreedApiModel
import com.example.catapult.breeds.db.BreedData
import com.example.catapult.breeds.list.model.BreedUiModel

fun BreedApiModel.asBreedDbModel(): BreedData {
    return BreedData(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description,
        temperament = this.temperament,
        origin = this.origin,
        life_span = this.life_span
    )
}

fun BreedData.asBreedUiModel(): BreedUiModel {
    return BreedUiModel(
        id = this.id,
        name = this.name,
        alternativeNames = this.alt_names,
        description = this.description,
        temperaments = this.temperament,
    )
}