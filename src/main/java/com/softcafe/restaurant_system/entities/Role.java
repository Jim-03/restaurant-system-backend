package com.softcafe.restaurant_system.entities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The roles of a user in the system")
public enum Role {
  WAITER,
  CASHIER,
  MANAGER
}
