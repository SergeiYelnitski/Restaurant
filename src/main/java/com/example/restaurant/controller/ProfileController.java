package com.example.restaurant.controller;

import com.example.restaurant.dto.UserDTO;
import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.User;
import com.example.restaurant.model.Vote;
import com.example.restaurant.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.example.restaurant.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "Profile Controller")
public class ProfileController {
  static final String REST_URL = "/api/profile";

  private final ProfileService profileService;

  @GetMapping
  @Operation(summary = "Get authorized user",
      responses = {
          @ApiResponse(description = "The user",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = User.class)))})
  public User get(@AuthenticationPrincipal AuthUser authUser) {
    return authUser.getUser();
  }


  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "User registration",
      description = "This can only be done by an unregistered user.",
      responses = {
          @ApiResponse(responseCode = "201", description = "Created",
              content = @Content(mediaType = "application/json",
                  examples = @ExampleObject(value = """
                      {
                       "name": "Alex",
                        "email": "alex@gmail.com",
                        "password": "password"
                      }"""),
                  schema = @Schema(implementation = UserDTO.class)))})
  public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO) {

    User created = profileService.register(userDTO);
    URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(REST_URL).build().toUri();

    return ResponseEntity.created(uriOfNewResource).body(created);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete user")
  public void delete(@AuthenticationPrincipal AuthUser authUser) {
    profileService.delete(authUser);
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Update user", responses = {
      @ApiResponse(responseCode = "204", description = "No content",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = """
                  {
                   "name": "Alexius",
                    "email": "alexius@gmail.com",
                    "password": "password"
                  }"""),
              schema = @Schema(implementation = UserDTO.class)))})
  public void update(@RequestBody @Valid UserDTO userDTO,
      @AuthenticationPrincipal AuthUser authUser) {
    profileService.update(userDTO, authUser);
  }

  @GetMapping("/votingHistory")
  @Operation(summary = "Get all user voices", description = "The necessary role is user.")
  public List<Vote> getVotingHistory(@AuthenticationPrincipal AuthUser authUser) {
    return profileService.getVotingHistory(authUser);
  }
}
