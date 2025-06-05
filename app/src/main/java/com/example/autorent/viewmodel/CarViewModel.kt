package com.example.autorent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autorent.api.CarApiController
import com.example.autorent.data.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarViewModel : ViewModel() {
    
    private val apiController = CarApiController()
    
    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        // Load some initial data - get Toyota cars as example
        loadCars(make = "toyota")
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
                    _cars.value = carList
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Unknown error occurred"
                }
            )
            
            _isLoading.value = false
        }
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
} 