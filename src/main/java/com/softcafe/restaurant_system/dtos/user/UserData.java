package com.softcafe.restaurant_system.dtos.user;

import com.softcafe.restaurant_system.entities.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "User's data")
public record UserData (
    @Schema(description = "Primary key", example = "1")
    Long id,
    @Schema(description = "Account username", example = "johnDoe")
    String username,
    @Schema(description = "Full legal name", example = "John Doe")
    String fullName,
    @Schema(description = "Valid Kenyan phone number", example = "0712345678")
    String phoneNumber,
    @Schema(description = "Email address", example = "john.doe@example.com")
    String email,
    @Schema(description = "Role of the user in the system", implementation = Role.class)
    Role role,
    @Schema(description = "Date added to the system", example = "11-11-2000:12:30:00")
    LocalDateTime createdAt,
    @Schema(description = "Last date the account was updated", example = "11-11-2000:12:30:00")
    LocalDateTime updatedAt
){

}
