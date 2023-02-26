package com.example.restaurant.service;

import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.Menu;
import com.example.restaurant.util.HasId;

import java.util.List;

public interface MenuService {

  List<Menu> getAllMenu(AuthUser authUser);

  Menu getMenu(AuthUser authUser, Integer id);

  void delete(int id);

  void update(AuthUser authUser, int id, Menu updateMenu);

  Menu createMenu(Menu menu, AuthUser authUser);
}
