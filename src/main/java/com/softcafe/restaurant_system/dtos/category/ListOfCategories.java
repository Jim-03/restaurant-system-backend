package com.softcafe.restaurant_system.dtos.category;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ListOfCategories(
    @ArraySchema(
        schema = @Schema(implementation = CategoryData.class),
        arraySchema = @Schema(description = "A list of category data")
    )
    List<CategoryData> categories
) {

}
