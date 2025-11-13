package com.softcafe.restaurant_system.repositories;

import com.softcafe.restaurant_system.entities.Role;
import com.softcafe.restaurant_system.entities.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByPhoneNumber(String phoneNumber);

  Page<User> findByFullNameContainingIgnoreCaseAndCreatedAtBetween(String name, LocalDateTime start,
      LocalDateTime end, Pageable pageable);

  Page<User> findByRoleAndCreatedAtBetween(Role role, LocalDateTime start, LocalDateTime end,
      Pageable pageable);

  Page<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

}
