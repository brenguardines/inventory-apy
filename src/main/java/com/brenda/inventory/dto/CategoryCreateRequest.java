package com.brenda.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequest {

  @NotBlank(message= "Category name is required")
  @Size(max = 50, message = "Category name must be at most 50 characters")
  private String name;
}
