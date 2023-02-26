package com.example.restaurant.controller;

import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.Menu;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.service.RestaurantService;
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
import org.springframework.web.bind.annotation.PathVariable;
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

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "Restaurant Controller")
public class RestaurantController {

  static final String REST_URL = "/api/restaurant";

  private final RestaurantService restaurantService;

  @GetMapping("/viewing")
  @Operation(summary = "Get all restaurants", description = "The necessary role is user.")
  public List<Restaurant> getAll() {
    return restaurantService.getAll();
  }

  @GetMapping("/viewing/{id}")
  @Operation(summary = "Get current menu of the selected restaurant.",
      description = "The necessary role is user.")
  public Menu getCurrentMenu(@PathVariable int id) {
    return restaurantService.getCurrentMenu(id);
  }

  @GetMapping
  @Operation(summary = "Get the restaurant where the admin is listed.",
      description = "The necessary role is admin.")
  public Restaurant get(@AuthenticationPrincipal AuthUser authUser) {
    return restaurantService.get(authUser);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Restaurant registration",
      description = "The necessary role is user. Do not specify in the menu when creating a restaurant.",
      responses = {@ApiResponse(responseCode = "201", description = "Created",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = """
                  {
                    "title": "Kebab",
                    "address": "Lithuania",
                    "telephone": "+3758825588"
                  }"""),
              schema = @Schema(implementation = Restaurant.class)))})
  public ResponseEntity<Restaurant> createWithLocation(@RequestBody @Valid Restaurant restaurant,
      @AuthenticationPrincipal AuthUser authUser) {

    Restaurant created = restaurantService.createWithLocation(restaurant, authUser);

    URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(REST_URL + "/{id}")
        .buildAndExpand(created.getId()).toUri();
    return ResponseEntity.created(uriOfNewResource).body(created);
  }


  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Update restaurant", description = "The necessary role is admin.",
      responses = {
          @ApiResponse(responseCode = "204", description = "No content",
              content = @Content(mediaType = "application/json",
                  examples = @ExampleObject(value = """
                      {
                        "title": "Kebabius",
                        "address": "Lithuania",
                        "telephone": "+3758825585"
                      }"""),
                  schema = @Schema(implementation = Restaurant.class)))})
  public void update(@Valid @RequestBody Restaurant updateRest,
      @AuthenticationPrincipal AuthUser authUser) {
    restaurantService.update(updateRest, authUser);
  }

  @DeleteMapping()
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete restaurant", description = "The necessary role is admin.")
  public void delete(@AuthenticationPrincipal AuthUser authUser) {
    restaurantService.delete(authUser);
  }
}

