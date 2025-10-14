package com.softcafe.restaurant_system.dtos.food;

import com.softcafe.restaurant_system.entities.FoodAvailability;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "New Food", description = "Object describing the structure of the request body when adding a new food item")
public record NewFood(
    @NotNull(message = "Provide the name of the food item")
    @Schema(description = "Food name", example = "fish")
    String name,
    @Schema(description = "A brief explanation of the food item", example = "deep fried with crispy taste")
    String description,
    @NotNull(message = "Provide the id of the category the food item belongs to!")
    @Schema(description = "Category's primary key", example = "1")
    int category_id,
    @Schema(description = "Link to the image file", example = "https://example.com/fish.png")
    String imageUrl,
    @NotNull(message = "Provide the price of the food item!")
    @Schema(description = "Food price in KSHS", example = "250")
    Double price,
    @NotNull(message = "Provide the food item's availability")
    @Schema(description = "The availability of the food item", implementation = FoodAvailability.class)
    FoodAvailability availability
) {

}
