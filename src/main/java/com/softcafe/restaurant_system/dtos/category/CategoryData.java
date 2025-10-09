package com.softcafe.restaurant_system.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "An object containing a category's data")
public record CategoryData(
    @Schema(description = "Primary key", example = "1")
    int id,
    @Schema(description = "Name", example = "Desserts")
    String name,
    @Schema(description = "A brief explanation of the category", example = "Meals/Snacks to be taken after the main meal")
    String description,
    @Schema(description = "Date first added", example = "2025-10-10T20:00:00")
    LocalDateTime createdAt,
    @Schema(description = "Date last updated", example = "2025-10-10T20:00:00")
    LocalDateTime updatedAt
) {

}
