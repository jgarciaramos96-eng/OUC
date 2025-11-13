package edu.uoc.epcsd.productcatalog.domain.service;

import edu.uoc.epcsd.productcatalog.domain.Product;
import edu.uoc.epcsd.productcatalog.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ItemService itemService;

    @Mock
    private ProductRepository productRepository;

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(itemService, productRepository);
    }

    @Test
    void createProductDelegatesToRepositoryAndReturnsIdentifier() {
        Product product = Product.builder()
                .id(null)
                .name("Camera")
                .description("Mirrorless camera")
                .dailyPrice(25.5)
                .brand("PhotoCorp")
                .model("X100")
                .categoryId(4L)
                .build();

        when(productRepository.createProduct(product)).thenReturn(99L);

        Long identifier = productService.createProduct(product);

        assertEquals(99L, identifier);
        verify(productRepository).createProduct(product);
    }

    @Test
    void createProductPropagatesRepositoryIllegalArgumentException() {
        Product product = Product.builder()
                .id(null)
                .name("Tripod")
                .description("Carbon fiber tripod")
                .dailyPrice(10.0)
                .brand("StableShot")
                .model("Pro-Lite")
                .categoryId(99L)
                .build();

        when(productRepository.createProduct(product)).thenThrow(new IllegalArgumentException("Unknown category"));

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(product));
    }
}
