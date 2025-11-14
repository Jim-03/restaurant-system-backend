package com.softcafe.restaurant_system.dtos.errors;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Object describing a server error response")
public record ServerError(
    @Schema(description = "Informative error message from the server", example = "An error has occurred")
    String message
) {

}
