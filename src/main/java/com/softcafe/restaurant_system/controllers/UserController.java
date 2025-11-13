package com.softcafe.restaurant_system.controllers;

import com.softcafe.restaurant_system.dtos.errors.InvalidError;
import com.softcafe.restaurant_system.dtos.errors.ServerError;
import com.softcafe.restaurant_system.dtos.user.ListOfUsers;
import com.softcafe.restaurant_system.dtos.user.NewUser;
import com.softcafe.restaurant_system.dtos.user.UpdateUser;
import com.softcafe.restaurant_system.dtos.user.UserData;
import com.softcafe.restaurant_system.entities.Role;
import com.softcafe.restaurant_system.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Controller layer containing endpoints handling user model")
@Validated
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  @Operation(summary = "Add user", description = "Add a new user to the system")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "User successfully added", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = UserData.class)
      )),
      @ApiResponse(responseCode = "400", description = "Invalid or missing user data", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = InvalidError.class)
      )),
      @ApiResponse(responseCode = "409", description = "User data violates unique constraint", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"A user with this email already exists!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
      ))
  })
  public ResponseEntity<UserData> add(
      @NotNull(message = "Provide the new user's data!")
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The new user's object data",
          required = true,
          content = @Content(
              schema = @Schema(implementation = NewUser.class)
          )
      )
      @Valid
      @RequestBody NewUser newUser
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(newUser));
  }

  @GetMapping
  @Operation(summary = "Find all users", description = "Search and filter users with various criteria")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User's list found", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = ListOfUsers.class)
      )),
      @ApiResponse(responseCode = "400", description = "Invalid or missing parameters", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = InvalidError.class)
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = ServerError.class)
      ))
  })
  public ResponseEntity<ListOfUsers> findAll(
      @Parameter(description = "A name substring to be searched", example = "Jam")
      @RequestParam("name") @Nullable String name,

      @Parameter(description = "User role filter", schema = @Schema(implementation = Role.class))
      @RequestParam("role") @Nullable Role role,

      @Parameter(description = "Starting date for employment date range", required = true)
      @RequestParam("start") @NotNull(message = "Provide the starting date!") LocalDateTime start,

      @Parameter(description = "Ending date for employment date range", required = true)
      @RequestParam("end") @NotNull(message = "Provide the ending date!") LocalDateTime end,

      @Parameter(description = "The sort order of the list", example = "ASC", schema = @Schema(allowableValues = {
          "ASC", "DESC"}))
      @RequestParam("sort") String sort,

      @Parameter(description = "The page to fetch from. 1-indexed", example = "1")
      @RequestParam("page") int page
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.getAll(name, role, start, end, sort, page));
  }

  @Operation(summary = "Update user", description = "Update an existing user's data")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User's data successfully updated", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = UserData.class)
      )),
      @ApiResponse(responseCode = "400", description = "Invalid or missing parameters", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = InvalidError.class)
      )),
      @ApiResponse(responseCode = "404", description = "User data not found", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User not found!\"}")
      )),
      @ApiResponse(responseCode = "409", description = "User's data validates unique constraint", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"A user with this email already exists\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = InternalError.class)
      ))
  })
  @PutMapping("/{id}")
  public ResponseEntity<UserData> update(
      @Parameter(description = "User's primary key", example = "1", required = true)
      @NotNull(message = "Provide the primary key!") @Min(value = 1, message = "Minimum ID value is 1!")
      @PathVariable Long id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Newly updated user data",
          required = true,
          content = @Content(schema = @Schema(implementation = UpdateUser.class))
      )
      @Valid
      @RequestBody UpdateUser updatedData
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, updatedData));
  }

  @Operation(summary = "Delete User", description = "Removes a user's details from the system")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User's details successfully deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid or missing parameter", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = InvalidError.class)
      )),
      @ApiResponse(responseCode = "404", description = "The specified user record wasn't found", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User not found!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = InternalError.class)
      ))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "User's primary key", example = "1", required = true)
      @NotNull(message = "Provide the user's primary key")
      @PathVariable Long id
  ) {
    userService.deleteUser(id);
    return ResponseEntity.ok().build();
  }
}
