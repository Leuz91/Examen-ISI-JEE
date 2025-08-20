package isi.eshop.web.dto;

import jakarta.validation.constraints.*;

public record CreateCategoryRequest(@NotBlank String name) {}
