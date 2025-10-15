package com.softcafe.restaurant_system.repositories;

import com.softcafe.restaurant_system.entities.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

  Optional<Category> findByNameIgnoreCase(String name);
}
