package com.example.autorent.api

import com.example.autorent.data.Car
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CarApiService {
    
    @GET("v1/cars")
    suspend fun getCars(
        @Header("X-Api-Key") apiKey: String,
        @Query("make") make: String? = null,
        @Query("model") model: String? = null,
        @Query("fuel_type") fuelType: String? = null,
        @Query("drive") drive: String? = null,
        @Query("cylinders") cylinders: Int? = null,
        @Query("transmission") transmission: String? = null,
        @Query("year") year: Int? = null,
        @Query("min_city_mpg") minCityMpg: Int? = null,
        @Query("max_city_mpg") maxCityMpg: Int? = null,
        @Query("min_hwy_mpg") minHwyMpg: Int? = null,
        @Query("max_hwy_mpg") maxHwyMpg: Int? = null,
        @Query("min_comb_mpg") minCombMpg: Int? = null,
        @Query("max_comb_mpg") maxCombMpg: Int? = null
    ): Response<List<Car>>
} 