package com.example.catapult.details.api.model


import kotlinx.serialization.Serializable

@Serializable
data class DetailApiModel(
    val id: String,
    val name: String, // ime rase
    val image: Image?, // slika rase (minimalno jedan treba)
    val description: String, // kompletan opis rase
    val origin: String, // spisak zemalja porekla
    val temperament: String, // sve osobine temperamenta
    val life_span: String, // životni vek
    val weight: Weight, // prosečna težina i/ili visina rase

    // 5 widgeta
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,

    val rare: Int, // da li je retka vrsta
    val wikipedia_url: String?, // dugme koje otvara stranicu o rasi na Vikipediji u browseru
)

@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)

@Serializable
data class Image(
    val id: String?
)
