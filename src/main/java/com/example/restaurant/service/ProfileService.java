package com.example.restaurant.service;

import com.example.restaurant.dto.UserDTO;
import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.User;
import com.example.restaurant.model.Vote;

import java.util.List;

public interface ProfileService {

  User register(UserDTO userDTO);

  void delete(AuthUser authUser);

  void update(UserDTO userDTO, AuthUser authUser);

  List<Vote> getVotingHistory(AuthUser authUser);
}
