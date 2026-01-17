package com.brenda.inventory.repositories;

import com.brenda.inventory.entity.Category;
import com.brenda.inventory.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void findByCategory_Name_shouldReturnProductsOfThatCategory() {
    // Arrange
    Category ropa = new Category();
    ropa.setName("ropa");
    ropa = categoryRepository.save(ropa);

    Category tecnologia = new Category();
    tecnologia.setName("tecnologia");
    tecnologia = categoryRepository.save(tecnologia);

    Product p1 = new Product();
    p1.setName("remera");
    p1.setDescription("algodon");
    p1.setPrice(1000);
    p1.setStock(10);
    p1.setCategory(ropa);

    Product p2 = new Product();
    p2.setName("pantalon");
    p2.setDescription("jean");
    p2.setPrice(2000);
    p2.setStock(5);
    p2.setCategory(ropa);

    Product p3 = new Product();
    p3.setName("mouse");
    p3.setDescription("inalambrico");
    p3.setPrice(3000);
    p3.setStock(7);
    p3.setCategory(tecnologia);

    productRepository.saveAll(List.of(p1, p2, p3));

    // Act
    List<Product> result = productRepository.findByCategory_Name("ropa");

    // Assert
    assertThat(result).hasSize(2);
    assertThat(result)
        .extracting(Product::getName)
        .containsExactlyInAnyOrder("remera", "pantalon");
  }

  @Test
  void findByCategory_Name_whenNoMatches_shouldReturnEmptyList() {
    List<Product> result = productRepository.findByCategory_Name("hogar");
    assertThat(result).isEmpty();
  }
}
