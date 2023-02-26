package com.example.restaurant.repository;

import com.example.restaurant.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {

  @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
  @Query("SELECT u FROM User u WHERE u.id=?1")
  User getUserWithRestaurant(int id);

  Optional<User> getByEmail(String email);

}

