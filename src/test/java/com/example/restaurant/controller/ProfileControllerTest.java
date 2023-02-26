package com.example.restaurant.controller;


import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.model.User;
import com.example.restaurant.dto.UserDTO;
import com.example.restaurant.util.JsonUtil;
import com.example.restaurant.util.UserUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant.controller.ProfileController.REST_URL;
import static com.example.restaurant.controller.TestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends AbstractControllerTest {

  @Autowired
  private UserRepository repository;

  @Test
  @WithUserDetails(value = USER_MAIL)
  void get() throws Exception {
    perform(MockMvcRequestBuilders.get(REST_URL))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(USER_MATCHER.contentJson(user));
  }

  @Test
  void getUnAuth() throws Exception {
    perform(MockMvcRequestBuilders.get(REST_URL))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void register() throws Exception {
    UserDTO newTo = new UserDTO(null, "newName", "newemail@ya.ru", "newPassword");
    User newUser = UserUtil.createNewFromTo(newTo);
    ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.writeValue(newTo)))
        .andDo(print())
        .andExpect(status().isCreated());

    User created = USER_MATCHER.readFromJson(action);
    int newId = created.id();
    newUser.setId(newId);
    USER_MATCHER.assertMatch(created, newUser);
    USER_MATCHER.assertMatch(repository.getById(newId), newUser);
  }

  @Test
  @WithUserDetails(value = USER_MAIL)
  void update() throws Exception {
    UserDTO updatedTo = new UserDTO(null, "newName", USER_MAIL, "newPassword");
    perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.writeValue(updatedTo)))
        .andDo(print())
        .andExpect(status().isNoContent());

    USER_MATCHER.assertMatch(repository.getById(USER_ID), UserUtil.updateFromTo(new User(user), updatedTo));
  }

  @Test
  void registerInvalid() throws Exception {
    UserDTO newTo = new UserDTO(null, null, null, null);
    perform(MockMvcRequestBuilders.post(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.writeValue(newTo)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(value = USER_MAIL)
  void updateInvalid() throws Exception {
    UserDTO updatedTo = new UserDTO(null, null, "password", null);
    perform(MockMvcRequestBuilders.put(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.writeValue(updatedTo)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(value = USER_MAIL)
  void updateDuplicate() throws Exception {
    UserDTO updatedTo = new UserDTO(null, "newName", ADMIN_MAIL, "newPassword");
    perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.writeValue(updatedTo)))
        .andDo(print())
        .andExpect(status().isBadRequest());

  }
}
