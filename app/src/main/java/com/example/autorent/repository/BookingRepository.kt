package com.example.autorent.repository

import com.example.autorent.database.Booking
import com.example.autorent.database.BookingDao
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class BookingRepository(private val bookingDao: BookingDao) {
    
    fun getAllBookings(): Flow<List<Booking>> {
        return bookingDao.getAllBookings()
    }
    
    fun getBookingsByStatus(status: String): Flow<List<Booking>> {
        return bookingDao.getBookingsByStatus(status)
    }
    
    suspend fun getBookingById(bookingId: String): Booking? {
        return bookingDao.getBookingById(bookingId)
    }
    
    suspend fun insertBooking(booking: Booking) {
        bookingDao.insertBooking(booking)
    }
    
    suspend fun updateBooking(booking: Booking) {
        bookingDao.updateBooking(booking)
    }
    
    suspend fun deleteBooking(booking: Booking) {
        bookingDao.deleteBooking(booking)
    }
    
    suspend fun updateBookingStatus(bookingId: String, status: String) {
        bookingDao.updateBookingStatus(bookingId, status)
    }
    
    // Helper functions for generating random booking data
    fun generateRandomPickupLocation(): String {
        val locations = listOf(
            "Universității nr. 1, Oradea, 410087, Bihor",
            "Republicii nr. 45, Oradea, 410073, Bihor",
            "Calea Aradului nr. 12, Oradea, 410222, Bihor",
            "Strada Mihai Eminescu nr. 23, Oradea, 410028, Bihor",
            "Bulevardul Dacia nr. 78, Oradea, 410053, Bihor",
            "Strada Ion Creangă nr. 34, Oradea, 410095, Bihor"
        )
        return locations.random()
    }
    
    fun generateRandomDates(): Pair<String, String> {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val today = Calendar.getInstance()
        
        // Start date: random between today and 7 days from now
        val startDaysFromNow = (0..7).random()
        today.add(Calendar.DAY_OF_YEAR, startDaysFromNow)
        val startDate = dateFormat.format(today.time)
        
        // End date: 1-7 days after start date
        val duration = (1..7).random()
        today.add(Calendar.DAY_OF_YEAR, duration)
        val endDate = dateFormat.format(today.time)
        
        return Pair(startDate, endDate)
    }
    
    fun generateRandomTimes(): Pair<String, String> {
        val hours = listOf("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00")
        val startTime = hours.random() + " AM"
        val endTime = hours.random() + " AM"
        return Pair(startTime, endTime)
    }
    
    fun calculateDuration(startDate: String, endDate: String): String {
        // Simple duration calculation - this could be more sophisticated
        return "2 days" // For now, keeping it simple as in the screenshot
    }
    
    fun calculatePricing(dailyPrice: Int, days: Int = 2): Triple<Double, Double, Double> {
        val dailyRate = dailyPrice.toDouble() * days
        val serviceFee = 25.0 // Fixed service fee
        val insurance = 30.0 // Fixed insurance
        return Triple(dailyRate, serviceFee, insurance)
    }
} 