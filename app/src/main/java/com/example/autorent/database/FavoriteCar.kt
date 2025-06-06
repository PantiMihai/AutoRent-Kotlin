package com.example.autorent.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cars")
data class FavoriteCar(
    @PrimaryKey
    val carId: String,
    val make: String?,
    val model: String?,
    val year: Int?,
    val carClass: String?,
    val cylinders: Int?,
    val displacement: Double?,
    val drive: String?,
    val fuelType: String?,
    val transmission: String?,
    val imageUrl: String,
    val dailyPrice: Int,
    val dateAdded: Long = System.currentTimeMillis()
) 