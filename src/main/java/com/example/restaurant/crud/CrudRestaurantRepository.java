package com.example.restaurant.crud;

import com.example.restaurant.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends BaseRepository<Restaurant> {

  List<Restaurant> findAll();

  Restaurant findByAdmin_Id(Integer id);

}
