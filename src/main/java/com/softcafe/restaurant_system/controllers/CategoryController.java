package com.softcafe.restaurant_system.controllers;

import com.softcafe.restaurant_system.dtos.category.CategoryData;
import com.softcafe.restaurant_system.dtos.category.ListOfCategories;
import com.softcafe.restaurant_system.dtos.category.NewCategory;
import com.softcafe.restaurant_system.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Validated
@Tag(name = "CategoryController", description = "Controller containing endpoints handling the category model")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Operation(description = "Fetches a list of all categories")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The list of categories successfully retrieved", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = ListOfCategories.class)
      ))
  })
  @GetMapping
  public ResponseEntity<ListOfCategories> getAll() {
    return ResponseEntity.ok(categoryService.getAllCategories());
  }

  @Operation(description = "Adds a new category")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Category successfully added", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = CategoryData.class)
      )),
      @ApiResponse(responseCode = "400", description = "Category data isn't provided", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Provide the category's details\"}")
      )),
      @ApiResponse(responseCode = "409", description = "Category violates unique constraint", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"A category with this name already exists!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
      ))
  })
  @PostMapping
  public ResponseEntity<CategoryData> addNewCategory(
      @Valid
      @NotNull(message = "Provide the new category's details")
      @RequestBody NewCategory newCategory
  ) {
    return ResponseEntity.status(201).body(categoryService.addNewCategory(newCategory));
  }

  @Operation(description = "Updates a category's details")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Category successfully updated", content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = CategoryData.class)
      )),
      @ApiResponse(responseCode = "400", description = "missing id/updated data", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Provide the category's id!\"}")
      )),
      @ApiResponse(responseCode = "404", description = "category record not found", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Category not found!\"}")
      )),
      @ApiResponse(responseCode = "409", description = "Updated data violates unique constraint", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"A category with this name already exists!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
      ))
  })
  @PutMapping("/{id}")
  public ResponseEntity<CategoryData> update(
      @NotNull(message = "Provide the category's id!")
      @NotNull(message = "Provide the updated data!")
      @Valid
  ) {
    return ResponseEntity.status(200).body(categoryService.updateCategory(id, newCategory));
  }

  @Operation(description = "Removes a category's data from the system")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Category successfully removed", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Category successfully deleted\"}")
      )),
      @ApiResponse(responseCode = "400", description = "Missing id", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Provide the category's id!\"}")
      )),
      @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"The specified category doesn't exist!\"}")
      )),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
          mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
      ))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> delete(
      @NotNull(message = "Provide the category's id!")
      @PathVariable int id
  ) {
    categoryService.deleteCategory(id);
    return ResponseEntity.status(200).body(Map.of("message", "successfully deleted the category"));
  }
}
