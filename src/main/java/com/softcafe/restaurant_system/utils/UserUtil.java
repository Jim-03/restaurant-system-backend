package com.softcafe.restaurant_system.utils;

import com.softcafe.restaurant_system.dtos.user.NewUser;
import com.softcafe.restaurant_system.dtos.user.UpdateUser;
import com.softcafe.restaurant_system.dtos.user.UserData;
import com.softcafe.restaurant_system.entities.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserUtil {

  /**
   * Converts a dto to a user object
   *
   * @param newUser User DTO
   * @return A user object
   */
  public static User toObject(NewUser newUser) {
    User user = new User();
    user.setUsername(newUser.username());
    user.setPassword(newUser.password());
    user.setFullName(newUser.fullName());
    user.setPhoneNumber(newUser.phoneNumber());
    user.setEmail(newUser.email());
    user.setRole(newUser.role());
    return user;
  }

  /**
   * Converts a user object to its dto
   *
   * @param user User object
   * @return A user dto
   */
  public static UserData toDto(User user) {
    return new UserData(
        user.getId(),
        user.getUsername(),
        user.getFullName(),
        user.getPhoneNumber(),
        user.getEmail(),
        user.getRole(),
        user.getCreatedAt(),
        user.getUpdatedAt()
    );
  }

  /**
   * Updates the data in a user object
   *
   * @param updatedData DTO containing updated data
   * @param user        Old user data
   */
  public static void update(UpdateUser updatedData, User user) {
    user.setUsername(updatedData.username());

    if (updatedData.password() != null && !updatedData.password().isBlank()) {
      user.setPassword(BCrypt.hashpw(updatedData.password(), BCrypt.gensalt()));
    }

    user.setEmail(updatedData.email());
    user.setPhoneNumber(updatedData.phoneNumber());
    user.setRole(updatedData.role());
    user.setFullName(updatedData.fullName());
  }
}
