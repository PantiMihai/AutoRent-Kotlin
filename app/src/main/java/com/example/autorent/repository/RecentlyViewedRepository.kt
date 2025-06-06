package com.example.autorent.repository

import com.example.autorent.data.Car
import com.example.autorent.database.RecentlyViewedCar
import com.example.autorent.database.RecentlyViewedCarDao
import kotlinx.coroutines.flow.Flow

class RecentlyViewedRepository(private val recentlyViewedCarDao: RecentlyViewedCarDao) {
    
    fun getRecentlyViewedCars(): Flow<List<RecentlyViewedCar>> {
        return recentlyViewedCarDao.getRecentlyViewedCars()
    }
    
    suspend fun addToRecentlyViewed(car: Car) {
        val recentlyViewedCar = RecentlyViewedCar(
            carId = car.id,
            make = car.make,
            model = car.model,
            year = car.year,
            carClass = car.carClass,
            cylinders = car.cylinders,
            displacement = car.displacement,
            drive = car.drive,
            fuelType = car.fuelType,
            transmission = car.transmission,
            imageUrl = car.imageUrl,
            dailyPrice = car.dailyPrice,
            lastViewedAt = System.currentTimeMillis()
        )
        recentlyViewedCarDao.insertRecentlyViewed(recentlyViewedCar)
    }
    
    suspend fun clearAllRecentlyViewed() {
        recentlyViewedCarDao.clearAllRecentlyViewed()
    }
    
    suspend fun isRecentlyViewed(carId: String): Boolean {
        return recentlyViewedCarDao.isRecentlyViewed(carId)
    }
} 