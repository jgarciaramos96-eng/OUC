package edu.uoc.epcsd.productcatalog.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.productcatalog.application.rest.request.CreateProductRequest;
import edu.uoc.epcsd.productcatalog.application.rest.request.UpdateProductRequest;
import edu.uoc.epcsd.productcatalog.domain.Product;
import edu.uoc.epcsd.productcatalog.domain.service.CategoryService;
import edu.uoc.epcsd.productcatalog.domain.service.ProductService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(ProductRESTController.class)
class ProductRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;
    
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
    void createProductCreatedResponse() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Cámara", "Cámara profesional", 45.0, "Canon", "X-Pro2000", 3L);

        when(productService.createProduct(any(Product.class))).thenReturn(7L);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/products/7"))
                .andExpect(content().string("7"));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).createProduct(productCaptor.capture());
        Product createdProduct = productCaptor.getValue();

        assertEquals("Cámara", createdProduct.getName());
        assertEquals("Cámara profesional", createdProduct.getDescription());
        assertEquals(45.0, createdProduct.getDailyPrice());
        assertEquals("Canon", createdProduct.getBrand());
        assertEquals("X-Pro2000", createdProduct.getModel());
        assertEquals(3L, createdProduct.getCategoryId());
    }

    @Test
    void createProductWhenCategoryDoesNotExist() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Cámara", "Cámara profesional", 45.0, "PhotoFilm", "X-Pro", 99L);

        when(productService.createProduct(any(Product.class))).thenThrow(new IllegalArgumentException("Missing category"));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProductInvalidBody() throws Exception {
        CreateProductRequest invalidRequest = new CreateProductRequest(
                "", "", null, "", "", null);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).createProduct(any(Product.class));
    }

    @Test
    void updateProductSuccess() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest( "Cámara", "Cámara profesional actualizada", 50.0, "Canon", "X-Pro3000", 3L);

        mockMvc.perform(put("/products/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).updateProduct(productCaptor.capture());
        Product updatedProduct = productCaptor.getValue();

        assertEquals("Cámara", updatedProduct.getName());
        assertEquals("Cámara profesional actualizada", updatedProduct.getDescription());
        assertEquals(50.0, updatedProduct.getDailyPrice());
        assertEquals("Canon", updatedProduct.getBrand());
        assertEquals("X-Pro3000", updatedProduct.getModel());
        assertEquals(3L, updatedProduct.getCategoryId());
    }


    @Test
    void updateProductCategoryDoesNotExist() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest( "Cámara", "Cámara profesional", 45.0, "Canon", "X-Pro2000", 99L);

        doThrow(new IllegalArgumentException("Missing category"))
                .when(productService).updateProduct(any(Product.class));

        mockMvc.perform(put("/products/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProductIdDoesNotExist() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest("Cámara", "Cámara profesional actualizada", 50.0, "Canon", "X-Pro3000", 3L);

        doThrow(new IllegalArgumentException("Product not found"))
                .when(productService).updateProduct(any(Product.class));

        mockMvc.perform(put("/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}