package com.example.catapult.breeds.api.model

import com.example.catapult.details.model.DetailsUiModel
import kotlinx.serialization.Serializable

@Serializable
data class BreedApiModel(
    val id: String,
    val name: String,
    val temperament: String,
    val alt_names: String = "", // Default to empty string if not present
    val origin: String,
    val description: String,
    val life_span: String,
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val rare: Int,
    val wikipedia_url: String? = null,
    val weight: Weight,
    val reference_image_id: String? = null,
    val url: String=""
    )

//@Serializable
//data class Weight(
//    val imperial: String,
//    val metric: String
//)
