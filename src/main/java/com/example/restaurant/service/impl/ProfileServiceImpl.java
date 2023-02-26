package com.example.restaurant.service.impl;

import com.example.restaurant.dto.UserDTO;
import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.User;
import com.example.restaurant.model.Vote;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.repository.VoteRepository;
import com.example.restaurant.service.ProfileService;
import com.example.restaurant.service.exception.IllegalRequestDataException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.restaurant.util.UserUtil.createNewFromTo;
import static com.example.restaurant.util.UserUtil.prepareToSave;
import static com.example.restaurant.util.UserUtil.updateFromTo;
import static com.example.restaurant.util.ValidationUtil.assureIdConsistent;
import static com.example.restaurant.util.ValidationUtil.checkNew;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)

public class ProfileServiceImpl implements ProfileService {

  private final UserRepository userRepository;

  private final VoteRepository voteRepository;

  @Override
  @Transactional
  public User register(UserDTO userDTO) {
    checkNew(userDTO);
    if (userRepository.getByEmail(userDTO.getEmail()).isPresent())
      throw new IllegalRequestDataException("This email has already existed.");
    return userRepository.save(prepareToSave(createNewFromTo(userDTO)));
  }

  @Override
  public void delete(AuthUser authUser) {
    log.info("delete {}", authUser);
    userRepository.delete(authUser.id());
  }

  @Override
  @Transactional
  public void update(UserDTO userDTO, AuthUser authUser) {
    log.info("update {}", authUser);

    assureIdConsistent(userDTO, authUser.id());
    User user = authUser.getUser();
    Optional<User> opt = userRepository.getByEmail(userDTO.getEmail());

    if (opt.isPresent() && !opt.get().id.equals(authUser.id()))
      throw new IllegalRequestDataException("This email has already existed.");

    else
      userRepository.save(prepareToSave(updateFromTo(user, userDTO)));
  }

  @Override
  public List<Vote> getVotingHistory(AuthUser authUser) {
    log.info("get votes from {}", authUser);
    return voteRepository.findAllByUserIdOrderByDateTimeDesc(authUser.id());
  }
}
