package com.brenda.inventory.controllers;

import com.brenda.inventory.dto.CategoryResponse;
import com.brenda.inventory.dto.ProductCreateRequest;
import com.brenda.inventory.dto.ProductResponse;
import com.brenda.inventory.dto.ProductUpdateRequest;
import com.brenda.inventory.exceptions.GlobalExceptionHandler;
import com.brenda.inventory.exceptions.ResourceNotFoundException;
import com.brenda.inventory.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class) // para que aplique el @ControllerAdvice en estos tests
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ProductService productService;

  @Test
  void getAll_withoutCategory_shouldReturnList() throws Exception {
    ProductResponse p1 = ProductResponse.builder()
        .id(1L)
        .name("remera")
        .description("algodon")
        .price(1000)
        .stock(10)
        .category(CategoryResponse.builder().id(1L).name("ropa").build())
        .build();

    ProductResponse p2 = ProductResponse.builder()
        .id(2L)
        .name("mouse")
        .description("inalambrico")
        .price(3000)
        .stock(7)
        .category(CategoryResponse.builder().id(2L).name("tecnologia").build())
        .build();

    when(productService.getAll(null)).thenReturn(List.of(p1, p2));

    mockMvc.perform(get("/products"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("remera"))
        .andExpect(jsonPath("$[0].category.name").value("ropa"));
  }

  @Test
  void getAll_withCategoryParam_shouldPassItToService() throws Exception {
    when(productService.getAll("ropa")).thenReturn(List.of());

    mockMvc.perform(get("/products").param("category", "ropa"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getById_whenNotFound_shouldReturn404() throws Exception {
    when(productService.getById(999L))
        .thenThrow(new ResourceNotFoundException("Product not found with id: 999"));

    mockMvc.perform(get("/products/{id}", 999))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Product not found with id: 999"));
  }

  @Test
  void create_whenInvalidBody_shouldReturn400WithFieldErrors() throws Exception {
    // name blank, price null, stock null, categoryId null -> deben explotar las validations
    ProductCreateRequest invalid = new ProductCreateRequest();
    invalid.setName(""); // @NotBlank

    mockMvc.perform(post("/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalid)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").exists())
        .andExpect(jsonPath("$.price").exists())
        .andExpect(jsonPath("$.stock").exists())
        .andExpect(jsonPath("$.categoryId").exists());
  }

  @Test
  void create_whenValid_shouldReturn201AndBody() throws Exception {
    ProductCreateRequest request = new ProductCreateRequest();
    request.setName("remera");
    request.setDescription("algodon");
    request.setPrice(1000);
    request.setStock(10);
    request.setCategoryId(1L);

    ProductResponse response = ProductResponse.builder()
        .id(1L)
        .name("remera")
        .description("algodon")
        .price(1000)
        .stock(10)
        .category(CategoryResponse.builder().id(1L).name("ropa").build())
        .build();

    when(productService.create(any(ProductCreateRequest.class))).thenReturn(response);

    mockMvc.perform(post("/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("remera"))
        .andExpect(jsonPath("$.category.name").value("ropa"));
  }

  @Test
  void update_whenValid_shouldReturn200AndBody() throws Exception {
    Long id = 1L;

    ProductUpdateRequest request = new ProductUpdateRequest();
    request.setName("remera nueva");
    request.setDescription("algodon peinado");
    request.setPrice(1500);
    request.setStock(20);
    request.setCategoryId(1L);

    ProductResponse response = ProductResponse.builder()
        .id(id)
        .name("remera nueva")
        .description("algodon peinado")
        .price(1500)
        .stock(20)
        .category(CategoryResponse.builder().id(1L).name("ropa").build())
        .build();

    when(productService.update(eq(id), any(ProductUpdateRequest.class))).thenReturn(response);

    mockMvc.perform(put("/products/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("remera nueva"))
        .andExpect(jsonPath("$.price").value(1500))
        .andExpect(jsonPath("$.category.name").value("ropa"));
  }

  @Test
  void update_whenNotFound_shouldReturn404() throws Exception {
    Long id = 999L;

    ProductUpdateRequest request = new ProductUpdateRequest();
    request.setName("x");
    request.setDescription("x");
    request.setPrice(1);
    request.setStock(1);
    request.setCategoryId(1L);

    when(productService.update(eq(id), any(ProductUpdateRequest.class)))
        .thenThrow(new ResourceNotFoundException("Product not found with id: " + id));

    mockMvc.perform(put("/products/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Product not found with id: " + id));
  }

  @Test
  void update_whenInvalidBody_shouldReturn400WithFieldErrors() throws Exception {
    Long id = 1L;

    // name blank, price null, stock null, categoryId null -> deben explotar las validations
    ProductUpdateRequest invalid = new ProductUpdateRequest();
    invalid.setName(""); // @NotBlank
    // invalid.setPrice(null);
    // invalid.setStock(null);
    // invalid.setCategoryId(null);

    mockMvc.perform(put("/products/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalid)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").exists())
        .andExpect(jsonPath("$.price").exists())
        .andExpect(jsonPath("$.stock").exists())
        .andExpect(jsonPath("$.categoryId").exists());
  }


  @Test
  void delete_whenExists_shouldReturn204() throws Exception {
    Long id = 1L;

    doNothing().when(productService).delete(id);

    mockMvc.perform(delete("/products/{id}", id))
        .andExpect(status().isNoContent());
  }

  @Test
  void delete_whenNotFound_shouldReturn404() throws Exception {
    Long id = 999L;

    doThrow(new ResourceNotFoundException("Product not found with id: " + id))
        .when(productService).delete(id);

    mockMvc.perform(delete("/products/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Product not found with id: " + id));
  }


}
