package com.softcafe.restaurant_system.services;

import com.softcafe.restaurant_system.dtos.category.CategoryData;
import com.softcafe.restaurant_system.dtos.category.ListOfCategories;
import com.softcafe.restaurant_system.dtos.category.NewCategory;
import com.softcafe.restaurant_system.entities.Category;
import com.softcafe.restaurant_system.repositories.CategoryRepository;
import com.softcafe.restaurant_system.utils.CategoryUtil;
import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

  private CategoryRepository categoryRepository;

  /**
   * Adds a new category to the system
   *
   * @param newCategory The new category's data
   * @return The newly created category data
   * @throws ResponseStatusException in case the new category data isn't provided
   */
  @Transactional
  public CategoryData addNewCategory(NewCategory newCategory) {
    if (newCategory == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the new category data!");
    }

    Category category = categoryRepository.saveAndFlush(CategoryUtil.toObject(newCategory));
    log.info("A new category with ID:{} has been added", category.getId());

    return CategoryUtil.toDto(category);
  }

  /**
   * Retrieves a list of all categories
   *
   * @return An object containing a list of all categories
   */
  public ListOfCategories getAllCategories() {
    return new ListOfCategories(
        categoryRepository.findAll()
            .stream().map(CategoryUtil::toDto)
            .collect(Collectors.toList()));
  }

  /**
   * Updates an existing category's data
   *
   * @param id          The record's primary key
   * @param updatedData The newly updated category data
   * @return Newly updated category data
   * @throws ResponseStatusException BAD_REQUEST - in case the updated data is missing CONFLICT - in
   *                                 case the new data violates unique constraint NOT_FOUND - in
   *                                 case record isn't found
   */
  @Transactional
  public CategoryData updateCategory(int id, NewCategory updatedData) {
    if (updatedData == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the updated data!");
    }

    // Check if the new data violates unique constraint
    if (categoryRepository.findByName(updatedData.name()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "Another category with this name already exists!");
    }

    // Fetch the old data
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "The specified category doesn't exist!"));

    CategoryUtil.updateCategory(category, updatedData);

    category = categoryRepository.saveAndFlush(category);

    log.info("Category wiht ID {} has been updated", category.getId());

    return CategoryUtil.toDto(category);
  }

  /**
   * Removes a category from the system
   *
   * @param id Record's primary key
   * @throws ResponseStatusException NOT_FOUND in case the specified category doesn't exist
   */
  @Transactional
  public void deleteCategory(int id) {
    // Fetch the category's details
    Category category = categoryRepository.findById(id).orElseThrow(() -> {
      return new ResponseStatusException(HttpStatus.NOT_FOUND,
          "The specified category doesn't exist!");
    });

    categoryRepository.delete(category);
    log.info("Category {} has been removed", category.getName());
  }

}
