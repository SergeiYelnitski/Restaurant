package com.example.restaurant.repository;

import com.example.restaurant.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {

  Menu getByDateAndRestaurantId(LocalDate date, Integer restaurantId);

  List<Menu> getAllByRestaurantIdOrderByDateDesc(Integer id);

  @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
  @Query("SELECT m FROM Menu m WHERE m.id=?1")
  Menu getWithDishes(Integer id);

}
