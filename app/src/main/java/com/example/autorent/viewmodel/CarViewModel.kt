package com.example.autorent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.autorent.api.CarApiController
import com.example.autorent.data.Car
import com.example.autorent.database.AutoRentDatabase
import com.example.autorent.repository.FavoriteRepository
import com.example.autorent.repository.RecentlyViewedRepository
import com.example.autorent.repository.BookingRepository
import com.example.autorent.database.Booking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarViewModel(application: Application) : AndroidViewModel(application) {
    
    private val apiController = CarApiController()
    private val database = AutoRentDatabase.getDatabase(application)
    private val favoriteRepository = FavoriteRepository(database.favoriteCarDao())
    private val recentlyViewedRepository = RecentlyViewedRepository(database.recentlyViewedCarDao())
    private val bookingRepository = BookingRepository(database.bookingDao())
    
    private val _allCars = MutableStateFlow<List<Car>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow("All Types")
    private val _selectedCarsForComparison = MutableStateFlow<Set<String>>(emptySet())
    
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()
    val favoriteCarIds = favoriteRepository.getFavoriteCarIds()
    val favoriteCars = favoriteRepository.getAllFavorites()
    val recentlyViewedCars = recentlyViewedRepository.getRecentlyViewedCars()
    val allBookings = bookingRepository.getAllBookings()
    val selectedCarsForComparison: StateFlow<Set<String>> = _selectedCarsForComparison.asStateFlow()
    
    private val _filteredCars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _filteredCars.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        // Load cars from different brands to get variety
        loadMultipleBrands()
    }
    
    fun loadCars(
        make: String? = null,
        model: String? = null,
        fuelType: String? = null,
        drive: String? = null,
        cylinders: Int? = null,
        transmission: String? = null,
        year: Int? = null,
        minCityMpg: Int? = null,
        maxCityMpg: Int? = null,
        minHwyMpg: Int? = null,
        maxHwyMpg: Int? = null,
        minCombMpg: Int? = null,
        maxCombMpg: Int? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            apiController.getCars(
                make = make,
                model = model,
                fuelType = fuelType,
                drive = drive,
                cylinders = cylinders,
                transmission = transmission,
                year = year,
                minCityMpg = minCityMpg,
                maxCityMpg = maxCityMpg,
                minHwyMpg = minHwyMpg,
                maxHwyMpg = maxHwyMpg,
                minCombMpg = minCombMpg,
                maxCombMpg = maxCombMpg
            ).fold(
                onSuccess = { carList ->
                    _allCars.value = carList
                    applyFilters()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Unknown error occurred"
                }
            )
            
            _isLoading.value = false
        }
    }
    
    private fun loadMultipleBrands() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val allLoadedCars = mutableListOf<Car>()
            val brands = listOf("toyota", "bmw", "mercedes", "porsche", "audi", "honda", "ford")
            
            for (brand in brands) {
                try {
                    apiController.getCarsByMake(brand).fold(
                        onSuccess = { carList ->
                            allLoadedCars.addAll(carList)
                        },
                        onFailure = { 
                            // Continue with other brands even if one fails
                        }
                    )
                } catch (e: Exception) {
                    // Continue loading other brands
                }
            }
            
            _allCars.value = allLoadedCars
            applyFilters()
            _isLoading.value = false
        }
    }
    
    private fun applyFilters() {
        val allCars = _allCars.value
        val query = _searchQuery.value
        val filter = _selectedFilter.value
        
        var filteredCars = allCars
        
        // Apply search filter
        if (query.isNotBlank()) {
            filteredCars = filteredCars.filter { car ->
                car.displayName.contains(query, ignoreCase = true) ||
                car.make?.contains(query, ignoreCase = true) == true ||
                car.model?.contains(query, ignoreCase = true) == true
            }
        }
        
        // Apply category filter
        if (filter != "All Types") {
            filteredCars = filteredCars.filter { car ->
                when (filter) {
                    "SUV" -> car.categoryType == "SUV"
                    "Sport" -> car.categoryType == "Sport"
                    "Sedan" -> car.categoryType == "Sedan"
                    "Electric" -> car.categoryType == "Electric"
                    else -> true
                }
            }
        }
        
        _filteredCars.value = filteredCars
    }
    
    fun searchCars(query: String) {
        _searchQuery.value = query
        applyFilters()
    }
    
    fun setFilter(filter: String) {
        _selectedFilter.value = filter
        applyFilters()
    }
    
    fun toggleFavorite(car: Car) {
        viewModelScope.launch {
            val isFavorite = favoriteRepository.isFavorite(car.id)
            if (isFavorite) {
                favoriteRepository.removeFromFavorites(car.id)
            } else {
                favoriteRepository.addToFavorites(car)
            }
        }
    }
    
    suspend fun isFavorite(carId: String): Boolean {
        return favoriteRepository.isFavorite(carId)
    }
    
    fun toggleCarSelection(carId: String) {
        val currentSelected = _selectedCarsForComparison.value.toMutableSet()
        if (currentSelected.contains(carId)) {
            currentSelected.remove(carId)
        } else if (currentSelected.size < 2) {
            currentSelected.add(carId)
        }
        _selectedCarsForComparison.value = currentSelected
    }
    
    fun clearComparison() {
        _selectedCarsForComparison.value = emptySet()
    }
    
    fun loadCarsByMake(make: String) {
        loadCars(make = make)
    }
    
    fun loadCarsByModel(model: String) {
        loadCars(model = model)
    }
    
    fun loadCarsByYear(year: Int) {
        loadCars(year = year)
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun addToRecentlyViewed(car: Car) {
        viewModelScope.launch {
            recentlyViewedRepository.addToRecentlyViewed(car)
        }
    }
    
    fun clearRecentlyViewed() {
        viewModelScope.launch {
            recentlyViewedRepository.clearAllRecentlyViewed()
        }
    }
    
    fun getFeaturedCars(): List<Car> {
        return cars.value.shuffled().take(6)
    }
    
    suspend fun createBooking(car: Car): String {
        val bookingId = "BK${System.currentTimeMillis()}"
        val (startDate, endDate) = bookingRepository.generateRandomDates()
        val (startTime, endTime) = bookingRepository.generateRandomTimes()
        val pickupLocation = bookingRepository.generateRandomPickupLocation()
        val duration = bookingRepository.calculateDuration(startDate, endDate)
        val (dailyRate, serviceFee, insurance) = bookingRepository.calculatePricing(car.dailyPrice)
        val totalAmount = dailyRate + serviceFee + insurance
        
        val booking = Booking(
            bookingId = bookingId,
            carId = car.id,
            carMake = car.make,
            carModel = car.model,
            carYear = car.year,
            carImageUrl = car.imageUrl,
            carClass = car.carClass,
            fuelType = car.fuelType,
            transmission = car.transmission,
            dailyPrice = car.dailyPrice,
            rating = car.rating,
            startDate = startDate,
            endDate = endDate,
            startTime = startTime,
            endTime = endTime,
            duration = duration,
            pickupLocation = pickupLocation,
            dailyRate = dailyRate,
            serviceFee = serviceFee,
            insurance = insurance,
            totalAmount = totalAmount,
            paymentMethod = "Credit card"
        )
        
        bookingRepository.insertBooking(booking)
        return bookingId
    }
    
    suspend fun getBookingById(bookingId: String): Booking? {
        return bookingRepository.getBookingById(bookingId)
    }
    
    suspend fun completeBooking(bookingId: String, rating: Float, review: String? = null) {
        val booking = bookingRepository.getBookingById(bookingId)
        booking?.let { currentBooking ->
            val updatedBooking = currentBooking.copy(
                status = "Completed",
                rating = rating
            )
            bookingRepository.updateBooking(updatedBooking)
        }
    }
} 