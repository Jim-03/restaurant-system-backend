package com.softcafe.restaurant_system.dtos.errors;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Invalid or missing data description object")
public record InvalidError(
    @Schema(description = "Informative message", example = "An error has been encountered while processing your request!")
    String message,
    @ArraySchema(
        schema = @Schema(example = "Missing data!"),
        arraySchema = @Schema(description = "A list of errors that have occurred")
    )
    String[] errors
) {

}
