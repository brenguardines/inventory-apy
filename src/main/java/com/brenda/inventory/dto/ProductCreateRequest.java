package com.brenda.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {
  @NotBlank(message = "Product name is required")
  @Size(max = 100, message = "Product name must be at most 100 chracters")
  private String name;

  @Size(max = 500, message = "Decription must be at most 100 chracters")
  private String description;

  @NotNull(message = "Price is required")
  @Min(value = 0, message = "Price must be >= 0")
  private Integer price;

  @NotNull(message = "Stock is required")
  @Min(value = 0, message = "Stock must be >= 0")
  private Integer stock;

  @NotNull(message = "CategoryId is required")
  private Long categoryId;
}
