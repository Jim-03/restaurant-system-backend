package com.softcafe.restaurant_system.dtos.user;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Structure of a response body to fetching a list of users")
public record ListOfUsers(
    @Schema(description = "Total number of expected pages", example = "30")
    long totalPages,
    @ArraySchema(
        schema = @Schema(implementation = UserData.class),
        arraySchema = @Schema(description = "A list of user's data")
    )
    List<UserData> data
) {

}
