package com.example.restaurant.service.impl;

import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.Menu;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.Role;
import com.example.restaurant.model.User;
import com.example.restaurant.repository.MenuRepository;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.service.RestaurantService;
import com.example.restaurant.service.exception.IllegalRequestDataException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.restaurant.util.ValidationUtil.assureIdConsistent;
import static com.example.restaurant.util.ValidationUtil.checkNew;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "restaurant")
public class RestaurantServiceImpl implements RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final UserRepository userRepository;
  private final MenuRepository menuRepository;

  @Override
  @Cacheable
  public List<Restaurant> getAll() {
    return restaurantRepository.findAll();
  }

  @Override
  public Menu getCurrentMenu(int id) {
    int menuId = menuRepository.getByDateAndRestaurantId(LocalDate.now(), id).id();
    Optional<Menu> menu = Optional.ofNullable(menuRepository.getWithDishes(menuId));
    if (menu.isPresent())
      return menu.get();
    else
      throw new IllegalRequestDataException("The restaurant has no menu for today.");
  }

  @Override
  public Restaurant get(AuthUser authUser) {
    return restaurantRepository.findByAdminId(authUser.id());
  }

  @Override
  @Transactional
  public Restaurant createWithLocation(Restaurant restaurant, AuthUser authUser) {
    log.info("create {}", restaurant);

    checkNew(restaurant);
    Assert.notNull(restaurant, "Restaurant must be not null");

    User updateUser = authUser.getUser();
    updateUser.setRoles(Collections.singleton(Role.ADMIN));
    restaurant.setAdmin(authUser.getUser());
    Restaurant created = restaurantRepository.save(restaurant);
    updateUser.setRestaurant(created);
    userRepository.save(updateUser);
    return created;
  }

  @Override
  @Transactional
  public void update(Restaurant updateRest, AuthUser authUser) {
    log.info("update {}", updateRest);

    Assert.notNull(updateRest, "Restaurant must be not null");

    Restaurant oldRest = restaurantRepository.findByAdminId(authUser.id());

    assureIdConsistent(updateRest, oldRest.id());

    oldRest.setTitle(updateRest.getTitle());
    oldRest.setAddress(updateRest.getAddress());
    oldRest.setTelephone(updateRest.getTelephone());

    restaurantRepository.save(oldRest);
  }

  @Override
  @Transactional
  public void delete(AuthUser authUser) {
    log.info("delete restaurant");
    Integer restId = getRestaurantId(authUser);

    User user = authUser.getUser();
    user.setRestaurant(null);
    user.setRoles(Collections.singleton(Role.USER));
    userRepository.save(user);

    restaurantRepository.delete(restId);
  }

  private Integer getRestaurantId(@AuthenticationPrincipal AuthUser authUser) {
    return userRepository.getUserWithRestaurant(authUser.id()).getRestaurant().id();
  }
}
