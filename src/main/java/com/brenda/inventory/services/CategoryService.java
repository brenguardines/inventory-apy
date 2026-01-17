package com.brenda.inventory.services;

import com.brenda.inventory.dto.CategoryCreateRequest;
import com.brenda.inventory.dto.CategoryResponse;
import com.brenda.inventory.dto.CategoryUpdateRequest;
import com.brenda.inventory.entity.Category;
import com.brenda.inventory.exceptions.ResourceNotFoundException;
import com.brenda.inventory.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public CategoryResponse create(CategoryCreateRequest request) {
    Category category = new Category();
    category.setName(request.getName());

    Category saved = categoryRepository.save(category);
    return toResponse(saved);
  }

  public List<CategoryResponse> getAll() {
    return categoryRepository.findAll()
        .stream()
        .map(this::toResponse)
        .toList();
  }

  public CategoryResponse getById(Long id) {
    Category category = getOrThrow(id);
    return toResponse(category);
  }

  public CategoryResponse update(Long id, CategoryUpdateRequest request){
    Category category = getOrThrow(id);
    category.setName(request.getName());

    Category updated = categoryRepository.save(category);
    return toResponse(updated);
  }

  public void delete(Long id) {
    Category category = getOrThrow(id);
    categoryRepository.delete(category);
  }

  private Category getOrThrow(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
  }

  private CategoryResponse toResponse(Category category){
    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .build();
  }
}
