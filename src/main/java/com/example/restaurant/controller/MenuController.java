package com.example.restaurant.controller;

import com.example.restaurant.model.AuthUser;
import com.example.restaurant.model.Menu;
import com.example.restaurant.service.MenuService;
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
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "Menu Controller", description = "The necessary role is admin.")
public class MenuController {

  static final String REST_URL = "/api/admin/restaurant/menu";

  private final MenuService menuService;

  @GetMapping
  @Operation(summary = "Get all menu of the restaurants.")
  public List<Menu> getAllMenu(@AuthenticationPrincipal AuthUser authUser) {
    return menuService.getAllMenu(authUser);
  }


  @GetMapping("/{id}")
  @Operation(summary = "Get menu")
  public Menu getMenu(@AuthenticationPrincipal AuthUser authUser, @PathVariable Integer id) {
    return menuService.getMenu(authUser, id);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Creating a new menu", description = "The menu is unique for one day.",
      responses = {
          @ApiResponse(responseCode = "201", description = "Created",
              content = @Content(mediaType = "application/json",
                  examples = @ExampleObject(value = """
                      {
                        "date": "2021-11-26",
                        "dishes": [
                          {
                            "title": "Shaverma wih shaurma",
                            "price": 17.5
                          },
                          {
                            "title": "Shaverma wih kebab",
                            "price": 17.5
                          }
                        ]
                      }"""),
                  schema = @Schema(implementation = Menu.class)))})
  public ResponseEntity<Menu> createMenu(@RequestBody @Valid Menu menu,
      @AuthenticationPrincipal AuthUser authUser) {

    Menu createdMenu = menuService.createMenu(menu, authUser);

    URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(REST_URL + "/{id}")
        .buildAndExpand(createdMenu.getId()).toUri();
    return ResponseEntity.created(uriOfNewResource).body(createdMenu);
  }

  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Update menu by id", description = "The menu is unique for one day.",
      responses = {
          @ApiResponse(responseCode = "204", description = "No content",
              content = @Content(mediaType = "application/json",
                  examples = @ExampleObject(value = """
                      {
                        "date": "2021-11-25",
                        "dishes": [
                          {
                            "title": "Shaverma wih nuggets",
                            "price": 17.5
                          },
                          {
                            "title": "Shaverma wih french fries",
                            "price": 17.5
                          }
                        ]
                      }"""),
                  schema = @Schema(implementation = Menu.class)))})
  public void update(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id,
      @RequestBody @Valid Menu updateMenu) {
    menuService.update(authUser, id, updateMenu);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete menu")
  public void delete(@PathVariable int id) {
    menuService.delete(id);
  }
}
