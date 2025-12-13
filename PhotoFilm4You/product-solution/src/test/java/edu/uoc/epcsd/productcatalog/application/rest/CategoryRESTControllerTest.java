package edu.uoc.epcsd.productcatalog.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.productcatalog.application.rest.request.CreateCategoryRequest;
import edu.uoc.epcsd.productcatalog.application.rest.request.UpdateCategoryRequest;
import edu.uoc.epcsd.productcatalog.domain.Category;
import edu.uoc.epcsd.productcatalog.domain.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryRESTController.class)
class CategoryRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder();
        }
    }

    @Test
    void createCategorySuccess() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest(null, "Electrónica", "Categoría de electrónica");

        when(categoryService.createCategory(any(Category.class))).thenReturn(1L);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/categories/1"))
                .andExpect(content().string("1"));

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryService).createCategory(categoryCaptor.capture());
        Category created = categoryCaptor.getValue();

        assertEquals("Electrónica", created.getName());
        assertEquals("Categoría de electrónica", created.getDescription());
        assertNull(created.getParentId());
    }

    @Test
    void updateCategorySuccess() throws Exception {
        UpdateCategoryRequest request = new UpdateCategoryRequest(1L, "Electrónica actualizada", "Actualización de descripción");

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryService).updateCategory(categoryCaptor.capture());
        Category updated = categoryCaptor.getValue();

        assertEquals("Electrónica actualizada", updated.getName());
        assertEquals("Actualización de descripción", updated.getDescription());
        assertEquals(1L, updated.getId());
    }

    @Test
    void updateCategoryNotFound() throws Exception {
        UpdateCategoryRequest request = new UpdateCategoryRequest(null, "Electrónica", "Descripción");

        doThrow(new IllegalArgumentException("Category not found"))
                .when(categoryService).updateCategory(any(Category.class));

        mockMvc.perform(put("/categories/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCategoryInvalidBody() throws Exception {
        CreateCategoryRequest invalidRequest = new CreateCategoryRequest(null,"", "");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).createCategory(any(Category.class));
    }
    
    @Test
    void deleteCategory_shouldReturnFalseWhenCategoryDoesNotExist() throws Exception {
    	when(categoryService.deleteCategory(1L)).thenReturn(false);
    	
    	mockMvc.perform(delete("/categories/1")).andExpect(status().isNotFound());
    	
    	verify(categoryService, times(1)).deleteCategory(1L);
    }
    
    @Test
    void deleteCategory_shouldThrowExceptionWhenSubcategoriesExist() throws Exception {
    	doThrow(new IllegalArgumentException("Cannot delete category with subcategories"))
    		.when(categoryService).deleteCategory(2L);
    	
    	mockMvc.perform(delete("/categories/2"))
    		.andExpect(status().isBadRequest())
    		.andExpect(status().reason("Cannot delete category with subcategories"));
    	
    	verify(categoryService, times(1)).deleteCategory(2L);
    }
    
    @Test
    void deleteCategory_shouldThrowExceptionWhenProductsExist() throws Exception {
    	doThrow(new IllegalArgumentException("Cannot delete category with assigned products"))
    		.when(categoryService).deleteCategory(3L);
    	
    	mockMvc.perform(delete("/categories/3"))
    		.andExpect(status().isBadRequest())
    		.andExpect(status().reason("Cannot delete category with assigned products"));
    	
    	verify(categoryService, times(1)).deleteCategory(3L);
    }
    
    @Test
    void deleteCategory_shouldReturnTrueWhenCategoryIsDeletable() throws Exception {
    	when(categoryService.deleteCategory(4L)).thenReturn(true);
    	
    	mockMvc.perform(delete("/categories/4")).andExpect(status().isNoContent());
    	
    	verify(categoryService, times(1)).deleteCategory(4L);
    }
}
