package com.example.catapult.breeds.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.catapult.breeds.api.model.Weight

@Entity
data class BreedData (
    @PrimaryKey val id: String,
    // pocetni ekran
    val name: String,
    val description: String,
    val temperament: String,
    val alt_names: String = "", // Default to empty string if not present
    val origin: String,
    val life_span: String,
    val wikipedia_url: String? = null,
    val reference_image_id: String? = null,
    // dodato
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val rare: Int,
    val weight: Weight,
    val imageUrl: String,
)


