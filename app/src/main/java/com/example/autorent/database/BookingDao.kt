package com.example.autorent.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    
    @Query("SELECT * FROM bookings ORDER BY bookingDate DESC")
    fun getAllBookings(): Flow<List<Booking>>
    
    @Query("SELECT * FROM bookings WHERE bookingId = :bookingId")
    suspend fun getBookingById(bookingId: String): Booking?
    
    @Query("SELECT * FROM bookings WHERE status = :status ORDER BY bookingDate DESC")
    fun getBookingsByStatus(status: String): Flow<List<Booking>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)
    
    @Update
    suspend fun updateBooking(booking: Booking)
    
    @Delete
    suspend fun deleteBooking(booking: Booking)
    
    @Query("DELETE FROM bookings WHERE bookingId = :bookingId")
    suspend fun deleteBookingById(bookingId: String)
    
    @Query("UPDATE bookings SET status = :status WHERE bookingId = :bookingId")
    suspend fun updateBookingStatus(bookingId: String, status: String)
} 