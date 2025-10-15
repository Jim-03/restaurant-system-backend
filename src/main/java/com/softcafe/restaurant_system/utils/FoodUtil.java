package com.softcafe.restaurant_system.utils;

import com.softcafe.restaurant_system.dtos.food.FoodData;
import com.softcafe.restaurant_system.dtos.food.NewFood;
import com.softcafe.restaurant_system.entities.Category;
import com.softcafe.restaurant_system.entities.Food;

public class FoodUtil {

  /**
   * Converts a food dto to an object
   *
   * @param newFood  Food dto
   * @param category Category the food belongs to
   * @return Food object from the dto
   */
  public static Food toObject(NewFood newFood, Category category) {
    Food food = new Food();
    food.setName(newFood.name());
    food.setDescription(newFood.description());
    food.setImageUrl(newFood.imageUrl());
    food.setPrice(newFood.price());
    food.setAvailability(newFood.availability());
    food.setCategory(category);
    return food;
  }

  /**
   * Converts a food object to a dto
   *
   * @param food Food object
   * @return Food Dto
   */
  public static FoodData toDto(Food food) {
    return new FoodData(
        food.getId(),
        food.getName(),
        food.getDescription(),
        food.getImageUrl(),
        food.getPrice(),
        food.getAvailability(),
        food.getCreatedAt(),
        food.getUpdatedAt()
    );
  }

  /**
   * Updates a food object's data
   *
   * @param food        Food object
   * @param updatedFood Object containing new data
   * @param category    Category details to which the food belongs to
   */
  public static void update(Food food, NewFood updatedFood, Category category) {
    food.setName(updatedFood.name());
    food.setDescription(updatedFood.description());
    food.setAvailability(updatedFood.availability());
    food.setCategory(category);
    food.setPrice(updatedFood.price());
    food.setImageUrl(updatedFood.imageUrl());
  }
}
