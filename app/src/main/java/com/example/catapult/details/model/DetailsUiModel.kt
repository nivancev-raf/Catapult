package com.example.catapult.details.model

import com.example.catapult.breeds.api.model.Weight

// stavari koj

data class DetailsUiModel(
    val id: String,
    val name: String, // ime rase
//    val image: Image?, // slika rase (minimalno jedan treba)
    val reference_image_id: String? = null,
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
    val imageUrl: String

)


data class Weight(
    val imperial: String,
    val metric: String
)



//data class Image(
//    val url: String?
//)









/*
* package com.example.catalist.details.api.model


import kotlinx.serialization.Serializable

@Serializable
data class DetailApiModel(
    val id: String,
    val name: String, // ime rase
    val image: Image? // slika rase (minimalno jedan treba)
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
    val url: String?
)

* */
