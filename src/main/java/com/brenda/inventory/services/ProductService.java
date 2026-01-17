package com.brenda.inventory.services;

import com.brenda.inventory.dto.CategoryResponse;
import com.brenda.inventory.dto.ProductCreateRequest;
import com.brenda.inventory.dto.ProductResponse;
import com.brenda.inventory.dto.ProductUpdateRequest;
import com.brenda.inventory.entity.Category;
import com.brenda.inventory.entity.Product;
import com.brenda.inventory.exceptions.ResourceNotFoundException;
import com.brenda.inventory.repositories.CategoryRepository;
import com.brenda.inventory.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  public ProductResponse create(ProductCreateRequest request) {
    Category category = getCategoryOrThrow(request.getCategoryId());

    Product product = new Product();
    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setPrice(request.getPrice());
    product.setStock(request.getStock());
    product.setCategory(category);

    Product saved = productRepository.save(product);
    return toResponse(saved);
  }

  public List<ProductResponse> getAll(String categoryName) {
    List<Product> products;

    if (categoryName == null || categoryName.isBlank()) {
      products = productRepository.findAll();
    } else {
      products = productRepository.findByCategory_Name(categoryName);
    }

    return products.stream()
        .map(this::toResponse)
        .toList();
  }

  public ProductResponse getById(Long id) {
    Product product = getProductOrThrow(id);
    return toResponse(product);
  }

  public ProductResponse update(Long id, ProductUpdateRequest request) {
    Product product = getProductOrThrow(id);
    Category category = getCategoryOrThrow(request.getCategoryId());

    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setPrice(request.getPrice());
    product.setStock(request.getStock());
    product.setCategory(category);

    Product updated = productRepository.save(product);
    return toResponse(updated);
  }

  public void delete(Long id) {
    Product product = getProductOrThrow(id);
    productRepository.delete(product);
  }

  private Product getProductOrThrow(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
  }

  private Category getCategoryOrThrow(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
  }

  private ProductResponse toResponse(Product product) {
    CategoryResponse categoryResponse = CategoryResponse.builder()
        .id(product.getCategory().getId())
        .name(product.getCategory().getName())
        .build();

    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .stock(product.getStock())
        .category(categoryResponse)
        .build();
  }
}
