package com.softcafe.restaurant_system.dtos.food;

import com.softcafe.restaurant_system.entities.FoodAvailability;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "Food Data", description = "Object describing the structure of a food data record")
public record FoodData(
    @Schema(description = "Primary key", example = "1")
    Long id,
    @Schema(description = "Food name", example = "fish")
    String name,
    @Schema(description = "A brief explanation of the food item", example = "deep fried with crispy taste")
    String description,
    @Schema(description = "Link to the image file", example = "https://example.com/fish.png")
    String imageUrl,
    @Schema(description = "Food price in KSHS", example = "250")
    Double price,
    @Schema(description = "The availability of the food item", implementation = FoodAvailability.class)
    FoodAvailability availability,
    @Schema(description = "Creation date", example = "2025-10-10T08:00:00")
    LocalDateTime createdAt,
    @Schema(description = "Date last updated", example = "2025-10-10T08:00:00")
    LocalDateTime updatedAt
) {

}
