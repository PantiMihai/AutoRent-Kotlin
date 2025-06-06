package com.example.autorent.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteCar::class, RecentlyViewedCar::class, Booking::class],
    version = 3,
    exportSchema = false
)
abstract class AutoRentDatabase : RoomDatabase() {
    
    abstract fun favoriteCarDao(): FavoriteCarDao
    abstract fun recentlyViewedCarDao(): RecentlyViewedCarDao
    abstract fun bookingDao(): BookingDao
    
    companion object {
        @Volatile
        private var INSTANCE: AutoRentDatabase? = null
        
        fun getDatabase(context: Context): AutoRentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AutoRentDatabase::class.java,
                    "autorent_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
} 