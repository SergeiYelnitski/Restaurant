package com.example.restaurant.controller;

import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.Vote;
import com.example.restaurant.service.VotingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping (value = VotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "Voting Controller", description = "The necessary role is user.")
public class VotingController {
  static final String REST_URL = "/api/voting";

  private final VotingService votingService;

  @PostMapping("/{id}")
  @Operation(summary = "Vote")
  public void voting(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
    votingService.voting(authUser, id);
  }

  @GetMapping("/vote")
  @Operation(summary = "Get the current votes")
  public List<Vote> getCurrentVoices() {
    return votingService.getCurrentVoices();
  }
}
