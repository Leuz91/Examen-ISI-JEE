package isi.eshop.web.dto;

import jakarta.validation.constraints.*;

public record CreateProductRequest(
  @NotBlank String name,
  @DecimalMin("0.0") double price,
  @NotNull Long categoryId
) {}
