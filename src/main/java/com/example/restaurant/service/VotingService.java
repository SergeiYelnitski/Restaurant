package com.example.restaurant.service;

import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.Vote;

import java.util.List;

public interface VotingService {

  void voting(AuthUser authUser, int id);

  List<Vote> getCurrentVoices();
}
