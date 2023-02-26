package com.example.restaurant.service.impl;

import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.Vote;
import com.example.restaurant.repository.VoteRepository;
import com.example.restaurant.service.VotingService;
import com.example.restaurant.service.exception.IllegalRequestDataException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class VotingServiceImpl implements VotingService {

  private final VoteRepository voteRepository;

  @Override
  @Transactional
  public void voting(AuthUser authUser, int id) {
    log.info("voting restaurant id {}", id);

    if(!LocalTime.now().isAfter(LocalTime.of(11, 0))){
      Optional<Vote> oldVoice = Optional.ofNullable(
          voteRepository.findByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanEqual(authUser.id(),
              LocalDate.now().atStartOfDay(),
              LocalDate.now().plus(1, ChronoUnit.DAYS).atStartOfDay()));
      oldVoice.ifPresent(voice -> voteRepository.delete(voice.id()));
      voteRepository.save(new Vote(null, authUser.id(), id, LocalDateTime.now()));

    }
    else throw new IllegalRequestDataException("Voting is over!");
  }

  @Override
  public List<Vote> getCurrentVoices() {
    return voteRepository
        .findAllByDateTimeGreaterThanEqualAndDateTimeLessThanEqualOrderByDateTimeDesc(
            LocalDate.now().atStartOfDay(),
            LocalDate.now().plus(1, ChronoUnit.DAYS).atStartOfDay());
  }
}
