package com.softcafe.restaurant_system.dtos.user;

import com.softcafe.restaurant_system.entities.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "The object structure for updating a user's data")
public record UpdateUser(
    @Schema(description = "Unique username", example = "johnDoe")
    @NotNull(message = "Provide the user's username!")
    String username,
    @Schema(description = "Protective password", example = "str0nGP4s%w0rd")
    String password,
    @Schema(description = "Full legal name", example = "John Doe")
    @NotNull(message = "Provide the user's full legal name!")
    String fullName,
    @Schema(description = "Unique Kenyan phone number", example = "0712345678")
    @NotNull(message = "Provide the user's phone number!")
    String phoneNumber,
    @Schema(description = "Email address", example = "johnDoe@example.com")
    String email,
    @Schema(description = "Role in the system", implementation = Role.class)
    @NotNull(message = "Provide the user's role in the system!")
    Role role
) {

}
