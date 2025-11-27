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
    void createProductOK() {
        Product product = Product.builder()
                .id(1L)
                .name("CamaraSUB")
                .description("Camara especial subacuática")
                .dailyPrice(45.5)
                .brand("Canon")
                .model("XSUB-100")
                .categoryId(4L)
                .build();

        when(productRepository.createProduct(product)).thenReturn(1234L);

        Long id = productService.createProduct(product);

        assertEquals(1234L, id);
        verify(productRepository).createProduct(product);
    }

    @Test
    void createProductInvalidCategory() {
        Product product = Product.builder()
                .id(1L)
                .name("TripodeFC")
                .description("Tripode de fribra de carbono")
                .dailyPrice(16.0)
                .brand("StableShot")
                .model("Pro-Lite")
                .categoryId(99L)
                .build();

        when(productRepository.createProduct(product)).thenThrow(new IllegalArgumentException("Unknown category"));

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(product));
    }
}
