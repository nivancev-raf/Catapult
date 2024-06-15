package com.example.catapult.breeds.db

import androidx.room.TypeConverter
import com.example.catapult.breeds.api.model.BreedApiModel
import com.example.catapult.breeds.api.model.Weight
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromWeight(weight: Weight): String {
        return Gson().toJson(weight)
    }

    @TypeConverter
    fun toWeight(value: String): Weight {
        val type = object : TypeToken<Weight>() {}.type
        return Gson().fromJson(value, type)
    }
}
