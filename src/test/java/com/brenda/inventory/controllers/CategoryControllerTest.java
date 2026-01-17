package com.brenda.inventory.controllers;

import com.brenda.inventory.dto.CategoryCreateRequest;
import com.brenda.inventory.dto.CategoryResponse;
import com.brenda.inventory.dto.CategoryUpdateRequest;
import com.brenda.inventory.exceptions.GlobalExceptionHandler;
import com.brenda.inventory.exceptions.ResourceNotFoundException;
import com.brenda.inventory.services.CategoryService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import(GlobalExceptionHandler.class)
class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CategoryService categoryService;

  @Test
  void getAll_shouldReturnList() throws Exception {
    when(categoryService.getAll()).thenReturn(List.of(
        CategoryResponse.builder().id(1L).name("ropa").build(),
        CategoryResponse.builder().id(2L).name("tecnologia").build()
    ));

    mockMvc.perform(get("/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("ropa"));
  }

  @Test
  void create_whenValid_shouldReturn201() throws Exception {
    CategoryCreateRequest request = new CategoryCreateRequest();
    request.setName("ropa");

    when(categoryService.create(any(CategoryCreateRequest.class)))
        .thenReturn(CategoryResponse.builder().id(1L).name("ropa").build());

    mockMvc.perform(post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("ropa"));
  }

  @Test
  void getById_whenExists_shouldReturn200AndBody() throws Exception {
    Long id = 1L;

    when(categoryService.getById(id))
        .thenReturn(CategoryResponse.builder().id(id).name("ropa").build());

    mockMvc.perform(get("/categories/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("ropa"));
  }


  @Test
  void getById_whenNotFound_shouldReturn404() throws Exception {
    when(categoryService.getById(999L))
        .thenThrow(new ResourceNotFoundException("Category not found with id: 999"));

    mockMvc.perform(get("/categories/{id}", 999))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Category not found with id: 999"));
  }

  @Test
  void update_whenValid_shouldReturn200AndBody() throws Exception {
    Long id = 1L;

    CategoryUpdateRequest request = new CategoryUpdateRequest();
    request.setName("ropa nueva");

    when(categoryService.update(eq(id), any(CategoryUpdateRequest.class)))
        .thenReturn(CategoryResponse.builder().id(id).name("ropa nueva").build());

    mockMvc.perform(put("/categories/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("ropa nueva"));
  }

  @Test
  void update_whenInvalidBody_shouldReturn400WithFieldErrors() throws Exception {
    Long id = 1L;

    CategoryUpdateRequest invalid = new CategoryUpdateRequest();
    invalid.setName(""); // @NotBlank

    mockMvc.perform(put("/categories/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalid)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").exists());
  }

  @Test
  void delete_whenExists_shouldReturn204() throws Exception {
    Long id = 1L;

    doNothing().when(categoryService).delete(id);

    mockMvc.perform(delete("/categories/{id}", id))
        .andExpect(status().isNoContent());
  }

  @Test
  void delete_whenNotFound_shouldReturn404() throws Exception {
    Long id = 999L;

    doThrow(new ResourceNotFoundException("Category not found with id: " + id))
        .when(categoryService).delete(id);

    mockMvc.perform(delete("/categories/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Category not found with id: " + id));
  }

}
