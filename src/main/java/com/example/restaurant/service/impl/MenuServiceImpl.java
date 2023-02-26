package com.example.restaurant.service.impl;

import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.Menu;
import com.example.restaurant.repository.MenuRepository;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.service.MenuService;
import com.example.restaurant.service.exception.IllegalRequestDataException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static com.example.restaurant.util.ValidationUtil.assureIdConsistent;
import static com.example.restaurant.util.ValidationUtil.checkNew;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "menu")
public class MenuServiceImpl implements MenuService {

  private final RestaurantRepository restaurantRepository;

  private final MenuRepository menuRepository;

  private final UserRepository userRepository;

  @Override
  @Cacheable
  public List<Menu> getAllMenu(AuthUser authUser) {
    return menuRepository.getAllByRestaurantIdOrderByDateDesc(getRestaurantId(authUser));
  }

  @Override
  public Menu getMenu(AuthUser authUser, Integer id) {
    return checkMenu(authUser, id);
  }

  @Override
  @CacheEvict(allEntries = true)
  public void delete(int id) {
    log.info("delete menu by id = {}", id);
    menuRepository.delete(id);
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void update(AuthUser authUser, int id, Menu updateMenu) {
    log.info("update menu {} with id ={}", updateMenu, id);

    Menu oldMenu = checkMenu(authUser, id);
    assureIdConsistent(updateMenu, id);
    Assert.notNull(updateMenu, "Menu must be not null");

    oldMenu.setDate(updateMenu.getDate());
    oldMenu.setDishes(updateMenu.getDishes());

    menuRepository.save(oldMenu);
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public Menu createMenu(Menu menu, AuthUser authUser) {
    log.info("create {}", menu);
    checkNew(menu);
    Assert.notNull(menu, "Menu must be not null");
    menu.setRestaurant(restaurantRepository.findByAdminId(authUser.id()));
    return menuRepository.save(menu);
  }

  private Integer getRestaurantId(AuthUser authUser) {
    return userRepository.getUserWithRestaurant(authUser.id()).getRestaurant().id();
  }

  private Menu checkMenu(AuthUser authUser, Integer id) {
    Optional<Menu> menu = Optional.ofNullable(menuRepository.getWithDishes(id));
    if (menu.isPresent() && menu.get().getRestaurant().id() == getRestaurantId(authUser)) {
      return menu.get();
    } else
      throw new IllegalRequestDataException("You don't have such a menu.");
  }
}
