package com.example.autorent.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCarDao {
    
    @Query("SELECT * FROM favorite_cars ORDER BY dateAdded DESC")
    fun getAllFavorites(): Flow<List<FavoriteCar>>
    
    @Query("SELECT * FROM favorite_cars WHERE carId = :carId")
    suspend fun getFavoriteById(carId: String): FavoriteCar?
    
    @Query("SELECT carId FROM favorite_cars")
    fun getFavoriteCarIds(): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteCar: FavoriteCar)
    
    @Delete
    suspend fun deleteFavorite(favoriteCar: FavoriteCar)
    
    @Query("DELETE FROM favorite_cars WHERE carId = :carId")
    suspend fun deleteFavoriteById(carId: String)
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cars WHERE carId = :carId)")
    suspend fun isFavorite(carId: String): Boolean
} 