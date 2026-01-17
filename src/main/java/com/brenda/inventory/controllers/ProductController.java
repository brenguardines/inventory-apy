package com.brenda.inventory.controllers;

import com.brenda.inventory.dto.ProductCreateRequest;
import com.brenda.inventory.dto.ProductResponse;
import com.brenda.inventory.dto.ProductUpdateRequest;
import com.brenda.inventory.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponse create(@Valid @RequestBody ProductCreateRequest request) {
    return productService.create(request);
  }

  @GetMapping
  public List<ProductResponse> getAll(@RequestParam(required = false) String category) {
    return productService.getAll(category);
  }

  @GetMapping("/{id}")
  public ProductResponse getById(@PathVariable Long id) {
    return productService.getById(id);
  }

  @PutMapping("/{id}")
  public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
    return productService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
     productService.delete(id);
  }
}
