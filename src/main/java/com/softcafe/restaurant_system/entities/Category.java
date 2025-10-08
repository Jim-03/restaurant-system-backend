package com.softcafe.restaurant_system.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

  @Id
  @GeneratedValue
  private int id;

  @Column(unique = true, nullable = false)
  private String name;

  @Column
  private String description;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  private void create() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  private void update() {
    this.updatedAt = LocalDateTime.now();
  }
}
