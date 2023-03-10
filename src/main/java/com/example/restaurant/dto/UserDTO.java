package com.example.restaurant.dto;

import com.example.restaurant.util.HasId;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDTO extends NamedDTO implements HasId {
  @Email
  @NotBlank
  @Size(max = 100)
  String email;

  @NotBlank
  @Size(min = 5, max = 32)
  String password;

  public UserDTO(Integer id, String name, String email, String password) {
    super(id, name);
    this.email = email;
    this.password = password;
  }
}
