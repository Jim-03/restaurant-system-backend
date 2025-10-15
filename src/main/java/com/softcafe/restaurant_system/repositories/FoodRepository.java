package com.softcafe.restaurant_system.repositories;

import com.softcafe.restaurant_system.entities.Category;
import com.softcafe.restaurant_system.entities.Food;
import com.softcafe.restaurant_system.entities.FoodAvailability;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

  Optional<Food> findByNameIgnoreCase(String name);

  Page<Food> findAllByCategory(Category category, Pageable pageable);

  Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);

  Page<Food> findByAvailability(FoodAvailability availability, Pageable pageable);
}
