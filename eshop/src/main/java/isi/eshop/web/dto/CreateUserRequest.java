package isi.eshop.web.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
  @NotBlank String firstName,
  @NotBlank String lastName,
  @Email String email
) {}
