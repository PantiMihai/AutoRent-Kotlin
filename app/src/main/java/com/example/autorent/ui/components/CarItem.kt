package com.example.autorent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.autorent.data.Car

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarItem(
    car: Car,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Car Make and Model (Primary info)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${car.make?.uppercase() ?: "Unknown"} ${car.model?.uppercase() ?: "Unknown"}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (car.year != null) {
                    Text(
                        text = car.year.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Car class
            if (car.carClass != null) {
                Text(
                    text = car.carClass.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            // Technical details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Engine info
                if (car.cylinders != null || car.displacement != null) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Engine",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        val engineText = buildString {
                            car.cylinders?.let { append("${it}cyl") }
                            if (car.cylinders != null && car.displacement != null) append(" • ")
                            car.displacement?.let { append("${it}L") }
                        }
                        if (engineText.isNotEmpty()) {
                            Text(
                                text = engineText,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                
                // Fuel Type
                if (car.fuelType != null) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Fuel",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = car.fuelType.uppercase(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                // Drive Type
                if (car.drive != null) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Drive",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = car.drive.uppercase(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            
            // Transmission
            if (car.transmission != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Transmission: ${if (car.transmission == "a") "Automatic" else if (car.transmission == "m") "Manual" else car.transmission.uppercase()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 