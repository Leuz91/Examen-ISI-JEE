package isi.eshop.web.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record CreateOrderRequest(
  @NotNull Long customerId,
  @Size(min = 1) List<Item> items
) {
  public record Item(@NotNull Long productId, @Min(1) int quantity) {}
}
