package com.softcafe.restaurant_system.services;

import com.softcafe.restaurant_system.dtos.user.ListOfUsers;
import com.softcafe.restaurant_system.dtos.user.NewUser;
import com.softcafe.restaurant_system.dtos.user.UpdateUser;
import com.softcafe.restaurant_system.dtos.user.UserData;
import com.softcafe.restaurant_system.entities.Role;
import com.softcafe.restaurant_system.entities.User;
import com.softcafe.restaurant_system.repositories.UserRepository;
import com.softcafe.restaurant_system.utils.UserUtil;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Adds a new user to the system
   *
   * @param newUser The new user's object data
   * @return An object containing the newly added user data
   * @throws ResponseStatusException CONFLICT in case the user's data violates unique constraint
   */
  @Transactional
  public UserData addUser(NewUser newUser) {
    // Check if user violates unique constraint
    if (userRepository.findByUsername(newUser.username()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "A user with this username already exists!");
    }
    if (userRepository.findByPhoneNumber(newUser.phoneNumber()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "A user with this phone number already exists!");
    }
    if (newUser.email() != null && userRepository.findByEmail(newUser.email()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "A user with this email already exists!");
    }

    // Convert the dto to a user object
    User user = UserUtil.toObject(newUser);

    // Hash the user's password
    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

    user = userRepository.saveAndFlush(user);

    log.info("A new user with id {} has been added", user.getId());

    return UserUtil.toDto(user);
  }

  /**
   * Fetches a list of users added between two date ranges
   *
   * @param name  An optional name of the users
   * @param role  Optional role of the users
   * @param start The starting date of the date range
   * @param end   The ending date of date range
   * @param sort  The sorting order
   * @param page  The page to fetch from
   * @return an object containing the list of users
   * @throws ResponseStatusException - BAD_REQUEST in case the parameters are invalid
   */
  public ListOfUsers getAll(String name, Role role, LocalDateTime start, LocalDateTime end,
      String sort, int page) {
    if (!sort.equalsIgnoreCase("ASC") && !sort.equalsIgnoreCase("DESC")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Provide a valid sort order: 'ASC' OR 'DESC'");
    }

    if (end.isBefore(start)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid date range!");
    }
    Page<User> users = null;

    PageRequest pageRequest = PageRequest.of(
        page - 1, 10,
        Sort.by(sort.equals("ASC") ? Direction.ASC : Direction.DESC, "createdAt")
    );

    // Case 1: Name is provided but no role
    if (name != null && !name.isBlank()) {
      users = userRepository.findByFullNameContainingIgnoreCaseAndCreatedAtBetween(name, start, end,
          pageRequest);
    } else

      // Case 2: Role is provided but no name
      if (role != null) {
        users = userRepository.findByRoleAndCreatedAtBetween(role, start, end, pageRequest);
      }

      // Case 3: No name and role
      else {
        users = userRepository.findByCreatedAtBetween(start, end, pageRequest);
      }
    return new ListOfUsers(users.getTotalPages(), users.map(UserUtil::toDto).stream().toList());
  }

  /**
   * Updates a user's details
   *
   * @param id          User's primary key
   * @param updatedData Newly updated data
   * @return The newly updated data object
   * @throws ResponseStatusException CONFLICT in case the updated data violates unique constraint
   * @throws ResponseStatusException NOT_FOUND in case the user's record wasn't found
   */
  @Transactional
  public UserData updateUser(Long id, UpdateUser updatedData) {
    // Fetch the user's details
    User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "User not found!"
    ));

    // Check if the updated data violates unique constraint
    if (updatedData.email() != null && userRepository.findByEmail(updatedData.email())
        .filter(u -> u.getId() != id).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "A user with this email already exists!");
    }

    if (userRepository.findByPhoneNumber(updatedData.phoneNumber())
        .filter(u -> u.getId() != id).isPresent()) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "A user with this phone number already exists!"
      );
    }

    if (userRepository.findByUsername(updatedData.username())
        .filter(u -> u.getId() != id).isPresent()) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "A user with this username already exists!"
      );
    }

    // Update user's data
    UserUtil.update(updatedData, user);

    user = userRepository.saveAndFlush(user);

    log.info("A user with id {} has been updated", user.getId());

    return UserUtil.toDto(user);
  }

  /**
   * Removes a user from the system
   *
   * @param id User's primary key
   */
  @Transactional
  public void deleteUser(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "User not found!"
    ));
    userRepository.delete(user);
  }
}