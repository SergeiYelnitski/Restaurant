package com.example.restaurant.util;

import com.example.restaurant.model.Role;
import com.example.restaurant.model.User;
import com.example.restaurant.dto.UserDTO;
import lombok.experimental.UtilityClass;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class UserUtil {

  public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public static User createNewFromTo(UserDTO userTo) {
    return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
  }

  public static User updateFromTo(User user, UserDTO userTo) {
    user.setName(userTo.getName());
    user.setEmail(userTo.getEmail().toLowerCase());
    user.setPassword(userTo.getPassword());
    return user;
  }

  public static User prepareToSave(User user) {
    user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
    user.setEmail(user.getEmail().toLowerCase());
    return user;
  }
}
