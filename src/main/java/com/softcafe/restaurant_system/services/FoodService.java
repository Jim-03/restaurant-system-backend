package com.softcafe.restaurant_system.services;

import com.softcafe.restaurant_system.dtos.food.FoodData;
import com.softcafe.restaurant_system.dtos.food.ListOfFood;
import com.softcafe.restaurant_system.dtos.food.NewFood;
import com.softcafe.restaurant_system.entities.Category;
import com.softcafe.restaurant_system.entities.Food;
import com.softcafe.restaurant_system.entities.FoodAvailability;
import com.softcafe.restaurant_system.repositories.CategoryRepository;
import com.softcafe.restaurant_system.repositories.FoodRepository;
import com.softcafe.restaurant_system.utils.FoodUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Validated
public class FoodService {

  private final FoodRepository foodRepository;
  private final CategoryRepository categoryRepository;

  public FoodService(FoodRepository foodRepository, CategoryRepository categoryRepository) {
    this.foodRepository = foodRepository;
    this.categoryRepository = categoryRepository;
  }

  /**
   * Adds a new food item to the system
   *
   * @param newFood New food data
   * @return A dto containing the details of the newly saved record
   * @throws ResponseStatusException BAD_REQUEST In case the food data is missing
   * @throws ResponseStatusException CONFLICT In case the food data violates unique constraint
   * @throws ResponseStatusException NOT_FOUND In case the food's category already exists
   */
  @Transactional
  public FoodData addNewFoodItem(@Valid NewFood newFood) {
    // Check if food item is provided
    if (newFood == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the new food's data!");
    }

    // Check if the new food item's name violates unique constraint
    if (foodRepository.findByNameIgnoreCase(newFood.name()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "");
    }

    // Check if the category exists
    Category category = categoryRepository.findById(newFood.category_id()).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified category wasn't found"));

    Food food = foodRepository.save(FoodUtil.toObject(newFood, category));
    log.info("A new food item with id {} has been added ", food.getId());

    return FoodUtil.toDto(food);
  }

  /**
   * Retrieves a list of food items in the same category
   *
   * @param id   The category's primary key
   * @param page The page to fetch from. 1-indexed
   * @return A list of food items
   * @throws ResponseStatusException NOT_FOUND in case the specified category doesn't exist
   * @throws ResponseStatusException BAD_REQUEST in case the category's id is equal to 0
   */
  public ListOfFood fetchFoodItemByCategory(int id, int page) {

    if (id == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid category id!");
    }

    // Check if category exists
    Category category = categoryRepository.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified category doesn't exist!")
    );

    // Fetch the list of food items
    Page<Food> foods = foodRepository.findAllByCategory(
        category,
        PageRequest.of(page - 1, 10, Sort.by(Direction.ASC, "createdAt")));

    return new ListOfFood(
        foods.getTotalPages(),
        foods.map(FoodUtil::toDto).stream().toList()
    );
  }

  /**
   * Retrieves a list of food items containing the same substring in its name
   *
   * @param name Substring to search for
   * @param page The page to search from. 1-indexed
   * @return An object containing the total number of expected pages and the list of food items
   * @throws ResponseStatusException BAD_REQUEST in case the name isn't provided
   */
  public ListOfFood fetchByName(String name, int page) {
    if (name == null || name.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the name to search for!");
    }

    Page<Food> foodPage = foodRepository.findByNameContainingIgnoreCase(
        name,
        PageRequest.of(page - 1, 10, Sort.by(
            Direction.DESC, "category"))
    );

    return new ListOfFood(
        foodPage.getTotalPages(),
        foodPage.map(FoodUtil::toDto).stream().toList()
    );
  }

  /**
   * Retrieves a list of food items with the same availability
   *
   * @param availability Availability to search for
   * @param page         Page to fetch from. 1-indexed
   * @return An object containing the total number of expected pages and the list of food items
   * @throws ResponseStatusException BAD_REQUEST in case the availability isn't provided
   */
  public ListOfFood fetchByAvailability(FoodAvailability availability, int page) {
    if (availability == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Provide the availability level to search for!");
    }

    Page<Food> foods = foodRepository.findByAvailability(
        availability,
        PageRequest.of(
            page - 1,
            10,
            Sort.by(Direction.ASC, "createdAt")
        )
    );

    return new ListOfFood(
        foods.getTotalPages(),
        foods.map(FoodUtil::toDto).stream().toList()
    );
  }

  /**
   * Updates a food item's record
   *
   * @param id          Food item's primary key
   * @param updatedFood An object containing the updated data
   * @return The newly updated food data
   * @throws ResponseStatusException BAD_REQUEST in case the updated data is missing or the id is
   *                                 invalid
   * @throws ResponseStatusException NOT_FOUND in case the food/category data isn't found
   */
  @Transactional
  public FoodData updateFoodItem(Long id, @Valid NewFood updatedFood) {
    if (updatedFood == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the updated data!");
    }
    if (id == null || id == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id!");
    }

    // Fetch the food item
    Food food = foodRepository.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified food item wasn't found!")
    );

    // Check if the updated name already exists
    if (foodRepository.findByNameIgnoreCase(updatedFood.name())
        .filter(existing -> !Objects.equals(existing.getId(), id)).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "A food item with this name already exists!");
    }

    // Check if the category specified exists
    Category category;

    if (updatedFood.category_id() != food.getCategory().getId()) {
      category = categoryRepository.findById(updatedFood.category_id()).orElseThrow(
          () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
              "The specified category doesn't exist!")
      );
    } else {
      category = food.getCategory();
    }

    FoodUtil.update(food, updatedFood, category);

    Food update = foodRepository.saveAndFlush(food);

    log.info("A food item with id {} has been updated", update.getId());

    return FoodUtil.toDto(update);
  }

  /**
   * Removes a food item from the system
   *
   * @param id Primary key
   * @throws ResponseStatusException BAD_REQUEST in case the id isn't provided
   * @throws ResponseStatusException NOT_FOUND if there's no record matching the id
   */
  @Transactional
  public void deleteFoodItem(Long id) {
    if (id == null || id == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id!");
    }

    // Fetch the food details
    Food food = foodRepository.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified food item doesn't exist!")
    );

    // Remove its record
    foodRepository.delete(food);
  }
}
