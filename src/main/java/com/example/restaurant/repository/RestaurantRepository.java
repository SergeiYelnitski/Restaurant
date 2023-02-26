package com.example.restaurant.repository;

import com.example.restaurant.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

  List<Restaurant> findAll();

  Restaurant findByAdminId(Integer id);

}
