package edu.uoc.epcsd.productcatalog.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.productcatalog.application.rest.request.CreateProductRequest;
import edu.uoc.epcsd.productcatalog.domain.Product;
import edu.uoc.epcsd.productcatalog.domain.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRESTController.class)
class ProductRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void createProductReturnsCreatedResponseWithLocation() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Cámara", "Cámara profesional", 45.0, "PhotoFilm", "X-Pro", 3L);

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
        assertEquals("PhotoFilm", createdProduct.getBrand());
        assertEquals("X-Pro", createdProduct.getModel());
        assertEquals(3L, createdProduct.getCategoryId());
    }

    @Test
    void createProductReturnsBadRequestWhenCategoryDoesNotExist() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Cámara", "Cámara profesional", 45.0, "PhotoFilm", "X-Pro", 99L);

        when(productService.createProduct(any(Product.class))).thenThrow(new IllegalArgumentException("Missing category"));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("99")));
    }

    @Test
    void createProductValidatesRequestBody() throws Exception {
        CreateProductRequest invalidRequest = new CreateProductRequest(
                "", "", null, "", "", null);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).createProduct(any(Product.class));
    }
}
