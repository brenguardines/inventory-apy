package com.brenda.inventory.repositories;

import com.brenda.inventory.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void save_and_findById_shouldWork() {
    Category category = new Category();
    category.setName("ropa");

    Category saved = categoryRepository.save(category);

    assertThat(saved.getId()).isNotNull();

    Category found = categoryRepository.findById(saved.getId()).orElse(null);
    assertThat(found).isNotNull();
    assertThat(found.getName()).isEqualTo("ropa");
  }
}
