package com.example.restaurant.service;

import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.Menu;
import com.example.restaurant.model.Restaurant;

import java.util.List;

public interface RestaurantService {

  List<Restaurant> getAll();

  Menu getCurrentMenu(int id);

  Restaurant get(AuthUser authUser);

  Restaurant createWithLocation(Restaurant restaurant, AuthUser authUser);

  void update(Restaurant updateRest, AuthUser authUser);

  void delete(AuthUser authUser);
}
