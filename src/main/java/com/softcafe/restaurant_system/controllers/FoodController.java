package com.softcafe.restaurant_system.controllers;

import com.softcafe.restaurant_system.dtos.food.FoodData;
import com.softcafe.restaurant_system.dtos.food.ListOfFood;
import com.softcafe.restaurant_system.dtos.food.NewFood;
import com.softcafe.restaurant_system.entities.FoodAvailability;
import com.softcafe.restaurant_system.services.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
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

@Validated
@RestController
@RequestMapping("/food")
@Tag(name = "Food Controller", description = "Endpoints handling food data")
public class FoodController {

  private final FoodService foodService;

  public FoodController(FoodService foodService) {
    this.foodService = foodService;
  }

  @Operation(summary = "Adds new food item to the system")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Food item successfully added", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = FoodData.class))
      ),
      @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Provide the food item's data!\"}")
      )),
      @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"The specified category wasn't found!\"}")
      )),
      @ApiResponse(responseCode = "409", description = "Food data violates unique constraint", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"A food item with this name already exists!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error occurred!\"}")
      ))
  })
  @PostMapping
  public ResponseEntity<FoodData> add(
      @NotNull(message = "Provide the food data!")
      @Valid
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The new food data",
          required = true,
          content = @Content(schema = @Schema(implementation = NewFood.class))
      )
      @RequestBody NewFood newFood
  ) {
    return ResponseEntity.status(201).body(foodService.addNewFoodItem(newFood));
  }

  @Operation(summary = "Retrieve a list of food items with the same availability level")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Food list found", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = ListOfFood.class)
      )),
      @ApiResponse(responseCode = "400", description = "Missing/invalid availability or page", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Provide the availability level to search for!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
      ))
  })
  @GetMapping("/availability")
  public ResponseEntity<ListOfFood> available(
      @NotNull(message = "Provide the availability to search for!")
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The availability level",
          required = true,
          content = @Content(schema = @Schema(implementation = FoodAvailability.class))
      )
      @RequestParam("availability") FoodAvailability availability,
      @NotNull(message = "Provide the page to search from!")
      @Min(value = 1, message = "Pages start from 1!")
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Page to fetch from. 1-indexed",
          required = true,
          content = @Content(schema = @Schema(example = "1"))
      )
      @RequestParam("page") int page
  ) {
    return ResponseEntity.status(200).body(foodService.fetchByAvailability(availability, page));
  }

  @Operation(summary = "Search for food item in the database")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Food list found", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = ListOfFood.class)
      )),
      @ApiResponse(responseCode = "400", description = "Missing name or page", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Provide the name to search for!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
      ))
  })
  @GetMapping("/search")
  public ResponseEntity<ListOfFood> search(
      @NotNull(message = "Provide the name to search for!")
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The name to search for",
          required = true,
          content = @Content(schema = @Schema(example = "fish"))
      )
      @RequestParam("name") String name,
      @NotNull(message = "Provide the page to search from!")
      @Min(value = 1, message = "Pages start from 1!")
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Page to fetch from. 1-indexed",
          required = true,
          content = @Content(schema = @Schema(example = "1"))
      )
      @RequestParam("page") int page

  ) {
    return ResponseEntity.status(200).body(foodService.fetchByName(name, page));
  }

  @Operation(summary = "Fetch food items in a category")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Food list found", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = ListOfFood.class)
      )),
      @ApiResponse(responseCode = "400", description = "Missing/Invalid id or page", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Provide the category's id!\"}")
      )),
      @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"The specified category doesn't exist!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
      ))
  })
  @GetMapping("/category/{id}")
  public ResponseEntity<ListOfFood> categorize(
      @NotNull(message = "Provide the category's id!")
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The category's id",
          required = true,
          content = @Content(schema = @Schema(example = "1"))
      )
      @PathVariable int id,
      @NotNull(message = "Provide the page to search from!")
      @Min(value = 1, message = "Pages start from 1!")
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Page to fetch from. 1-indexed",
          required = true,
          content = @Content(schema = @Schema(example = "1"))
      )
      @RequestParam("page") int page
  ) {
    return ResponseEntity.status(200).body(foodService.fetchFoodItemByCategory(id, page));
  }

  @Operation(summary = "Update a food item")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Food item updated", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = FoodData.class)
      )),
      @ApiResponse(responseCode = "400", description = "Missing/Invalid id or updated data", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Provide the updated data!\"}")
      )),
      @ApiResponse(responseCode = "404", description = "Food item not found", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"The specified food item doesn't exist!\"}")
      )),
      @ApiResponse(responseCode = "409", description = "Food data violates unique constraint", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"A food item with this name already exists!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
      ))
  })
  @PutMapping("/{id}")
  public ResponseEntity<FoodData> update(
      @Min(value = 1, message = "Provide a valid id!")
      @NotNull(message = "Provide a valid id!")
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The food item's primary key",
          required = true,
          content = @Content(schema = @Schema(example = "1"))
      )
      @PathVariable Long id,
      @NotNull(message = "Provide the food data!")
      @Valid
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The new food data",
          required = true,
          content = @Content(schema = @Schema(implementation = NewFood.class))
      )
      @RequestBody NewFood updated
  ) {
    return ResponseEntity.status(200).body(foodService.updateFoodItem(id, updated));
  }

  @Operation(summary = "Remove a food item")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Food item deleted", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = FoodData.class)
      )),
      @ApiResponse(responseCode = "400", description = "Missing/Invalid id", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Provide the food item's id!\"}")
      )),
      @ApiResponse(responseCode = "404", description = "Food item not found", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"The specified food item doesn't exist!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
      ))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> delete(
      @Min(value = 1, message = "Provide a valid id!")
      @NotNull(message = "Provide a valid id!")
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The food item's primary key",
          required = true,
          content = @Content(schema = @Schema(example = "1"))
      )
      @PathVariable Long id
  ) {
    foodService.deleteFoodItem(id);
    return ResponseEntity.status(200).body(Map.of("message", "Food item successfully deleted"));
  }

}
