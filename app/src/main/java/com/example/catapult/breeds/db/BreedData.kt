package com.example.catapult.breeds.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
)

