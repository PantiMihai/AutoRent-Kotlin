package com.example.autorent.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyViewedCarDao {
    
    @Query("SELECT * FROM recently_viewed_cars ORDER BY lastViewedAt DESC LIMIT 10")
    fun getRecentlyViewedCars(): Flow<List<RecentlyViewedCar>>
    
    @Query("SELECT * FROM recently_viewed_cars WHERE carId = :carId")
    suspend fun getRecentlyViewedById(carId: String): RecentlyViewedCar?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentlyViewed(recentlyViewedCar: RecentlyViewedCar)
    
    @Query("DELETE FROM recently_viewed_cars")
    suspend fun clearAllRecentlyViewed()
    
    @Query("DELETE FROM recently_viewed_cars WHERE carId = :carId")
    suspend fun deleteRecentlyViewedById(carId: String)
    
    @Query("SELECT EXISTS(SELECT 1 FROM recently_viewed_cars WHERE carId = :carId)")
    suspend fun isRecentlyViewed(carId: String): Boolean
} 