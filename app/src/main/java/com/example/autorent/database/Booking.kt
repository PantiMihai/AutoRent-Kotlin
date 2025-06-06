package com.example.autorent.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey
    val bookingId: String,
    val carId: String,
    val carMake: String?,
    val carModel: String?,
    val carYear: Int?,
    val carImageUrl: String,
    val carClass: String?,
    val fuelType: String?,
    val transmission: String?,
    val dailyPrice: Int,
    val rating: Float,
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val duration: String,
    val pickupLocation: String,
    val dailyRate: Double,
    val serviceFee: Double,
    val insurance: Double,
    val totalAmount: Double,
    val paymentMethod: String,
    val bookingDate: Long = System.currentTimeMillis(),
    val status: String = "Confirmed" // Confirmed, Cancelled, Completed
) 