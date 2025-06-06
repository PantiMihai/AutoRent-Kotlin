package com.example.autorent.data

import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("class")
    val carClass: String?,
    val cylinders: Int?,
    val displacement: Double?,
    val drive: String?,
    @SerializedName("fuel_type")
    val fuelType: String?,
    val make: String?,
    val model: String?,
    val transmission: String?,
    val year: Int?
) {
    // Unique ID for the car
    val id: String
        get() = "${make ?: "unknown"}_${model ?: "unknown"}_${year ?: 0}".lowercase().replace(" ", "_")
    
    // Computed properties for UI
    val displayName: String
        get() = "${make?.replaceFirstChar { it.uppercase() } ?: "Unknown"} ${model?.replaceFirstChar { it.uppercase() } ?: "Unknown"}"
    
    val categoryType: String
        get() = when {
            // Electric vehicles (fuel type based)
            fuelType?.contains("electricity", ignoreCase = true) == true -> "Electric"
            
            // SUV detection
            carClass?.contains("suv", ignoreCase = true) == true ||
            carClass?.contains("sport utility", ignoreCase = true) == true ||
            model?.contains("suv", ignoreCase = true) == true -> "SUV"
            
            // Sport cars detection  
            carClass?.contains("sport", ignoreCase = true) == true ||
            carClass?.contains("performance", ignoreCase = true) == true ||
            model?.contains("sport", ignoreCase = true) == true ||
            (cylinders ?: 0) >= 8 -> "Sport"
            
            // Sedan detection
            carClass?.contains("sedan", ignoreCase = true) == true ||
            carClass?.contains("midsize", ignoreCase = true) == true ||
            carClass?.contains("compact", ignoreCase = true) == true ||
            carClass?.contains("large", ignoreCase = true) == true -> "Sedan"
            
            // Fallback
            else -> carClass?.replaceFirstChar { it.uppercase() } ?: "Car"
        }
    
    val imageUrl: String
        get() = getImageUrlForCar(make, model)
    
    val dailyPrice: Int
        get() = generateDailyPrice(make, carClass, year)
    
    val rating: Float
        get() = generateRating(make, year)
    
    val maxSpeed: String
        get() = generateMaxSpeed(make, carClass)
    
    val seatCount: String
        get() = generateSeatCount(carClass)
    
    val motorType: String
        get() = generateMotorType(fuelType, cylinders)
    
    val range: String
        get() = generateRange(fuelType, carClass)
    
    val transmissionType: String
        get() = if (transmission == "a") "Automatic" else if (transmission == "m") "Manual" else "Automatic"
    
    val chargingTime: String
        get() = generateChargingTime(fuelType)
}

private fun getImageUrlForCar(make: String?, model: String?): String {
    return when (make?.lowercase()) {
        "tesla" -> "https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=400&h=250&fit=crop"
        "bmw" -> "https://images.unsplash.com/photo-1555215695-3004980ad54e?w=400&h=250&fit=crop"
        "mercedes", "mercedes-benz" -> "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=400&h=250&fit=crop"
        "porsche" -> "https://images.unsplash.com/photo-1503736334956-4c8f8e92946d?w=400&h=250&fit=crop"
        "audi" -> "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=400&h=250&fit=crop"
        "toyota" -> "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=400&h=250&fit=crop"
        "honda" -> "https://images.unsplash.com/photo-1627603992792-d59063abe19f?w=400&h=250&fit=crop"
        "ford" -> "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=400&h=250&fit=crop"
        "chevrolet" -> "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=400&h=250&fit=crop"
        "nissan" -> "https://images.unsplash.com/photo-1580414155951-d2a340ba4aaf?w=400&h=250&fit=crop"
        "volkswagen" -> "https://images.unsplash.com/photo-1606016159991-7e91b4154b56?w=400&h=250&fit=crop"
        "hyundai" -> "https://images.unsplash.com/photo-1559416523-140ddc3d238c?w=400&h=250&fit=crop"
        "mazda" -> "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=400&h=250&fit=crop"
        "subaru" -> "https://images.unsplash.com/photo-1544636331-e26879cd4d9b?w=400&h=250&fit=crop"
        else -> "https://images.unsplash.com/photo-1502877338535-766e1452684a?w=400&h=250&fit=crop"
    }
}

private fun generateDailyPrice(make: String?, carClass: String?, year: Int?): Int {
    val basePrice = when (make?.lowercase()) {
        "tesla" -> 89
        "bmw" -> 120
        "mercedes", "mercedes-benz" -> 180
        "porsche" -> 250
        "audi" -> 140
        "toyota" -> 65
        "honda" -> 60
        "ford" -> 75
        "chevrolet" -> 70
        "nissan" -> 58
        "volkswagen" -> 68
        "hyundai" -> 55
        "mazda" -> 62
        "subaru" -> 72
        else -> 80
    }
    
    val classMultiplier = when {
        carClass?.contains("sport", ignoreCase = true) == true -> 1.4
        carClass?.contains("performance", ignoreCase = true) == true -> 1.5
        carClass?.contains("suv", ignoreCase = true) == true || 
        carClass?.contains("sport utility", ignoreCase = true) == true -> 1.3
        carClass?.contains("luxury", ignoreCase = true) == true -> 1.6
        carClass?.contains("compact", ignoreCase = true) == true -> 0.8
        carClass?.contains("large", ignoreCase = true) == true -> 1.2
        else -> 1.0
    }
    
    val yearMultiplier = when {
        (year ?: 2000) >= 2020 -> 1.2
        (year ?: 2000) >= 2015 -> 1.1
        (year ?: 2000) >= 2010 -> 1.0
        else -> 0.8
    }
    
    return (basePrice * classMultiplier * yearMultiplier).toInt()
}

private fun generateRating(make: String?, year: Int?): Float {
    val baseRating = when (make?.lowercase()) {
        "tesla" -> 4.8f
        "bmw" -> 4.6f
        "mercedes", "mercedes-benz" -> 4.7f
        "porsche" -> 4.9f
        "audi" -> 4.5f
        "toyota" -> 4.4f
        "honda" -> 4.3f
        "ford" -> 4.1f
        "chevrolet" -> 4.0f
        "nissan" -> 4.2f
        else -> 4.2f
    }
    
    val yearBonus = if ((year ?: 2000) >= 2020) 0.1f else 0.0f
    return (baseRating + yearBonus).coerceAtMost(5.0f)
}

private fun generateMaxSpeed(make: String?, carClass: String?): String {
    return when {
        carClass?.contains("sport", ignoreCase = true) == true -> "${(280..350).random()} km/h"
        make?.lowercase() == "tesla" -> "250 km/h"
        make?.lowercase() == "porsche" -> "${(300..350).random()} km/h"
        make?.lowercase() == "bmw" -> "${(240..280).random()} km/h"
        carClass?.contains("suv", ignoreCase = true) == true -> "${(200..240).random()} km/h"
        else -> "${(180..220).random()} km/h"
    }
}

private fun generateSeatCount(carClass: String?): String {
    return when {
        carClass?.contains("sport", ignoreCase = true) == true -> "2 Seats"
        carClass?.contains("suv", ignoreCase = true) == true -> "${(5..7).random()} Seats"
        carClass?.contains("compact", ignoreCase = true) == true -> "4 Seats"
        else -> "5 Seats"
    }
}

private fun generateMotorType(fuelType: String?, cylinders: Int?): String {
    return when {
        fuelType?.contains("electricity", ignoreCase = true) == true -> "Electric Motor"
        (cylinders ?: 0) >= 8 -> "V${cylinders} Engine"
        (cylinders ?: 0) >= 6 -> "V${cylinders} Engine"
        (cylinders ?: 0) >= 4 -> "${cylinders}-Cylinder"
        else -> "4-Cylinder"
    }
}

private fun generateRange(fuelType: String?, carClass: String?): String {
    return when {
        fuelType?.contains("electricity", ignoreCase = true) == true -> "${(250..400).random()} miles"
        carClass?.contains("sport", ignoreCase = true) == true -> "${(300..450).random()} miles"
        carClass?.contains("suv", ignoreCase = true) == true -> "${(350..500).random()} miles"
        else -> "${(400..550).random()} miles"
    }
}

private fun generateChargingTime(fuelType: String?): String {
    return if (fuelType?.contains("electricity", ignoreCase = true) == true) {
        "${(30..60).random()} min"
    } else {
        "3 min"
    }
} 