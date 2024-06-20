package com.example.catapult.breeds.mappers

import com.example.catapult.breeds.api.model.BreedApiModel
import com.example.catapult.breeds.db.BreedData
import com.example.catapult.breeds.list.model.BreedUiModel
import com.example.catapult.details.model.DetailsUiModel

fun BreedApiModel.asBreedDbModel(): BreedData {
    return BreedData(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description,
        temperament = this.temperament,
        origin = this.origin,
        life_span = this.life_span,
        reference_image_id = this.reference_image_id,
        wikipedia_url = this.wikipedia_url,
        adaptability = this.adaptability,
        affection_level = this.affection_level,
        child_friendly = this.child_friendly,
        dog_friendly = this.dog_friendly,
        energy_level = this.energy_level,
        rare = this.rare,
        weight = this.weight,
        imageUrl = this.url
    )
}

//fun BreedData.asBreedUiModel(): BreedUiModel {
//    return BreedUiModel(
//        id = this.id,
//        name = this.name,
//        alternativeNames = this.alt_names,
//        description = this.description,
//        temperaments = this.temperament,
//    )
//}

fun BreedApiModel.asBreedUiModel() = DetailsUiModel(
    id = this.id,
    name = this.name,
    reference_image_id = this.reference_image_id, // Assuming that Image in API model and UI model are the same type
    description = this.description,
    origin = this.origin,
    temperament = this.temperament,
    life_span = this.life_span,
    weight = this.weight, // Assuming that Weight in API model and UI model are the same type
    adaptability = this.adaptability,
    affection_level = this.affection_level,
    child_friendly = this.child_friendly,
    dog_friendly = this.dog_friendly,
    energy_level = this.energy_level,
    wikipedia_url = this.wikipedia_url,
    rare = this.rare, // You might need to adjust the type or the way you map this field
    imageUrl = this.url
)

fun BreedApiModel.toDetailsUiModel(): DetailsUiModel {
    return DetailsUiModel(
        id = this.id,
        name = this.name,
        reference_image_id = this.reference_image_id,
        description = this.description,
        origin = this.origin,
        temperament = this.temperament,
        life_span = this.life_span,
        weight = this.weight,
        adaptability = this.adaptability,
        affection_level = this.affection_level,
        child_friendly = this.child_friendly,
        dog_friendly = this.dog_friendly,
        energy_level = this.energy_level,
        wikipedia_url = this.wikipedia_url,
        rare = this.rare,
        imageUrl = this.url
    )
}

// breedData to breedUIModel
fun BreedData.asBreedUiModel(): BreedUiModel {
    return BreedUiModel(
        id = this.id,
        name = this.name,
        alternativeNames = this.alt_names,
        description = this.description,
        temperaments = this.temperament,
    )
}


// asBreedApiModel

//fun BreedData.asBreedApiModel(): BreedApiModel {
//    return BreedApiModel(
//        id = this.id,
//        name = this.name,
//        alt_names = this.alt_names,
//        description = this.description,
//        temperament = this.temperament,
//        origin = this.origin,
//        life_span = this.life_span,
//        reference_image_id = this.reference_image_id,
//        wikipedia_url = this.wikipedia_url
//    )
//}

