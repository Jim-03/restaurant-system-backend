package com.softcafe.restaurant_system.dtos.food;

import java.util.List;

public record ListOfFood(
    long totalPages,
    List<FoodData> data
) {

}
