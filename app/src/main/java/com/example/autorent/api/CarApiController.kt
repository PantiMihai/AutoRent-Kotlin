package com.example.autorent.api

import com.example.autorent.data.Car
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * CarApiController - Controller for accessing the Cars API from api-ninjas.com
 * 
 * API Documentation:
 * Base URL: https://api.api-ninjas.com/
 * 
 * Available Parameters (Non-Premium):
 * - make: Vehicle manufacturer (e.g. audi or toyota)
 * - model: Vehicle model (e.g. a4 or corolla)
 * - fuel_type: Type of fuel used. Possible values: gas, diesel, electricity
 * - drive: Drive transmission. Possible values: fwd (front-wheel drive), rwd (rear-wheel drive), awd (all-wheel drive), 4wd (four-wheel drive)
 * - cylinders: Number of cylinders in engine. Possible values: 2, 3, 4, 5, 6, 8, 10, 12, 16
 * - transmission: Type of transmission. Possible values: manual, automatic
 * - year: Vehicle model year (e.g. 2018)
 * - min_city_mpg: Minimum city fuel consumption (in miles per gallon)
 * - max_city_mpg: Maximum city fuel consumption (in miles per gallon)
 * - min_hwy_mpg: Minimum highway fuel consumption (in miles per gallon)
 * - max_hwy_mpg: Maximum highway fuel consumption (in miles per gallon)
 * - min_comb_mpg: Minimum combination (city and highway) fuel consumption (in miles per gallon)
 * - max_comb_mpg: Maximum combination (city and highway) fuel consumption (in miles per gallon)
 * 
 * Premium Only Parameters (Not available with current API key):
 * - limit: How many results to return. Must be between 1 and 50. Default is 1
 * - offset: Number of results to skip. Used for pagination. Default is 0
 * 
 * Response Fields:
 * - class: Vehicle class category (e.g. "midsize car", "suv", etc.)
 * - cylinders: Number of cylinders in the engine
 * - displacement: Engine displacement in liters
 * - drive: Drive type (e.g. "fwd" for front-wheel drive, "awd" for all-wheel drive, etc.)
 * - fuel_type: Type of fuel the vehicle uses (e.g. "gas", "diesel", etc.)
 * - make: Vehicle manufacturer (e.g. "toyota")
 * - model: Vehicle model name (e.g. "camry")
 * - transmission: Transmission type (e.g. "a" for automatic, "m" for manual)
 * - year: Vehicle model year
 * 
 * Premium Only Response Fields (Not available with current API key):
 * - city_mpg: City fuel consumption in miles per gallon
 * - highway_mpg: Highway fuel consumption in miles per gallon
 * - combination_mpg: Combined city and highway fuel consumption in miles per gallon
 */
class CarApiController {
    
    companion object {
        private const val BASE_URL = "https://api.api-ninjas.com/"
        private const val API_KEY = "UqIy6EAWZrrgtcoav9+hSA==dfbnGvdGSiaf3oqG"
    }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val apiService = retrofit.create(CarApiService::class.java)
    
    /**
     * Fetch cars from the API with optional parameters
     * Note: Without premium subscription, only 1 result is returned by default
     */
    suspend fun getCars(
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
    ): Result<List<Car>> {
        return try {
            val response = apiService.getCars(
                apiKey = API_KEY,
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
            )
            
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Convenience method to get cars by make (manufacturer)
     */
    suspend fun getCarsByMake(make: String): Result<List<Car>> {
        return getCars(make = make)
    }
    
    /**
     * Convenience method to get cars by model
     */
    suspend fun getCarsByModel(model: String): Result<List<Car>> {
        return getCars(model = model)
    }
    
    /**
     * Convenience method to get cars by year
     */
    suspend fun getCarsByYear(year: Int): Result<List<Car>> {
        return getCars(year = year)
    }
} 