package com.example.restaurant.repository;

import com.example.restaurant.model.Vote;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

  Vote findByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanEqual(Integer userId, LocalDateTime start,
      LocalDateTime end );

  List<Vote> findAllByUserIdOrderByDateTimeDesc(Integer userId);

  List<Vote> findAllByDateTimeGreaterThanEqualAndDateTimeLessThanEqualOrderByDateTimeDesc(LocalDateTime start,
      LocalDateTime end);

}
