package com.softcafe.restaurant_system.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewCategory(
    @Schema(description = "Name", example = "Desserts")
    @NotNull(message = "Provide the name of the category!")
    @NotEmpty(message = "Category name shouldn't be empty!")
    String name,
    @Schema(description = "A brief explanation of the category", example = "Meals/Snacks to be taken after the main meal")
    String description
) {

}
