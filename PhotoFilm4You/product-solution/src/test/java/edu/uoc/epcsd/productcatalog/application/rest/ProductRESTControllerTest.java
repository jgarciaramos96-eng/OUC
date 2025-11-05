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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    void createProductReturnsCreatedResponseWithLocationHeader() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Camera",
                "An amazing camera",
                15.0,
                "BrandX",
                "ModelY",
                2L
        );

        when(productService.createProduct(any(Product.class))).thenReturn(42L);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/products/42"))
                .andExpect(content().string("42"));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).createProduct(productCaptor.capture());
        Product product = productCaptor.getValue();
        assertThat(product.getId()).isNull();
        assertThat(product.getName()).isEqualTo(request.getName());
        assertThat(product.getDescription()).isEqualTo(request.getDescription());
        assertThat(product.getDailyPrice()).isEqualTo(request.getDailyPrice());
        assertThat(product.getBrand()).isEqualTo(request.getBrand());
        assertThat(product.getModel()).isEqualTo(request.getModel());
        assertThat(product.getCategoryId()).isEqualTo(request.getCategoryId());
    }

    @Test
    void createProductReturnsBadRequestWhenCategoryIsInvalid() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Camera",
                "An amazing camera",
                15.0,
                "BrandX",
                "ModelY",
                999L
        );

        when(productService.createProduct(any(Product.class))).thenThrow(new IllegalArgumentException("Invalid category"));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
