package com.softcafe.restaurant_system.entities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Food Availability", description = "Availability of a food item")
public enum FoodAvailability {
  IN_STOCK,
  LOW,
  OUT_OF_STOCK
}
