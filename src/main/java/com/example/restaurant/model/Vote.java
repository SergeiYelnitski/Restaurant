package com.example.restaurant.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "voice")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(name = "restaurant_id", nullable = false)
  private Integer restaurantId;

  @Column(name = "date_time", nullable = false)
  @NotNull
  private LocalDateTime dateTime;

  public Vote(Integer id, Integer userId, Integer restaurantId, LocalDateTime dateTime) {
    super(id);
    this.userId = userId;
    this.restaurantId = restaurantId;
    this.dateTime = dateTime;
  }
}
