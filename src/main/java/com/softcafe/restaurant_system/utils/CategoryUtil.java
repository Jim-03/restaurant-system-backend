package com.softcafe.restaurant_system.utils;

import com.softcafe.restaurant_system.dtos.category.CategoryData;
import com.softcafe.restaurant_system.dtos.category.NewCategory;
import com.softcafe.restaurant_system.entities.Category;

public class CategoryUtil {

  /**
   * Converts a DTO to a category instance
   *
   * @param newCategory Category DTO
   * @return A category instance
   */
  public static Category toObject(NewCategory newCategory) {
    Category category = new Category();
    category.setName(newCategory.name());
    if (newCategory.description() != null) {
      category.setDescription(newCategory.description());
    }
    return category;
  }

  /**
   * Converts a category instance to a DTO
   *
   * @param category Category instance
   * @return Category DTO
   */
  public static CategoryData toDto(Category category) {
    return new CategoryData(
        category.getId(),
        category.getName(),
        category.getDescription(),
        category.getCreatedAt(),
        category.getUpdatedAt()
    );
  }

  /**
   * Updates the details of a category instance
   *
   * @param category    The category object
   * @param updatedData The object to update from
   */
  public static void updateCategory(Category category, NewCategory updatedData) {
    category.setName(updatedData.name());
    category.setDescription(updatedData.description());
  }
}
