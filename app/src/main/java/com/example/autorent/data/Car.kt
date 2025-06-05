package com.example.autorent.data

import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("class")
    val carClass: String?,
    val cylinders: Int?,
    val displacement: Double?,
    val drive: String?,
    @SerializedName("fuel_type")
    val fuelType: String?,
    val make: String?,
    val model: String?,
    val transmission: String?,
    val year: Int?
) 