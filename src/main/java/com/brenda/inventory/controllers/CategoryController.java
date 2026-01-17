package com.brenda.inventory.controllers;

import com.brenda.inventory.dto.CategoryCreateRequest;
import com.brenda.inventory.dto.CategoryResponse;
import com.brenda.inventory.dto.CategoryUpdateRequest;
import com.brenda.inventory.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest request) {
    return categoryService.create(request);
  }

  @GetMapping
  public List<CategoryResponse> getAll() {
    return categoryService.getAll();
  }

  @GetMapping("/{id}")
  public CategoryResponse getById(@PathVariable Long id) {
    return categoryService.getById(id);
  }

  @PutMapping("/{id}")
  public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryUpdateRequest request) {
    return categoryService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    categoryService.delete(id);
  }
}
