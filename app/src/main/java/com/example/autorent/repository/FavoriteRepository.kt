package com.example.autorent.repository

import com.example.autorent.data.Car
import com.example.autorent.database.FavoriteCar
import com.example.autorent.database.FavoriteCarDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepository(private val favoriteCarDao: FavoriteCarDao) {
    
    fun getAllFavorites(): Flow<List<Car>> {
        return favoriteCarDao.getAllFavorites().map { favoriteCarList ->
            favoriteCarList.map { favoriteCar ->
                favoriteCar.toCar()
            }
        }
    }
    
    fun getFavoriteCarIds(): Flow<List<String>> {
        return favoriteCarDao.getFavoriteCarIds()
    }
    
    suspend fun addToFavorites(car: Car) {
        val favoriteCar = FavoriteCar(
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
            dailyPrice = car.dailyPrice
        )
        favoriteCarDao.insertFavorite(favoriteCar)
    }
    
    suspend fun removeFromFavorites(carId: String) {
        favoriteCarDao.deleteFavoriteById(carId)
    }
    
    suspend fun isFavorite(carId: String): Boolean {
        return favoriteCarDao.isFavorite(carId)
    }
}

// Extension function to convert FavoriteCar to Car
private fun FavoriteCar.toCar(): Car {
    return Car(
        carClass = this.carClass,
        cylinders = this.cylinders,
        displacement = this.displacement,
        drive = this.drive,
        fuelType = this.fuelType,
        make = this.make,
        model = this.model,
        transmission = this.transmission,
        year = this.year
    )
} 