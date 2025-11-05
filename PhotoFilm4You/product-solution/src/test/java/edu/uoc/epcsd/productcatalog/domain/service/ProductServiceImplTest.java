package edu.uoc.epcsd.productcatalog.domain.service;

import edu.uoc.epcsd.productcatalog.domain.Product;
import edu.uoc.epcsd.productcatalog.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ItemService itemService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createProductShouldDelegateToRepositoryAndReturnId() {
        Product product = Product.builder()
                .id(1L)
                .name("Camera")
                .description("An amazing camera")
                .dailyPrice(12.5)
                .brand("BrandX")
                .model("ModelY")
                .categoryId(2L)
                .build();

        when(productRepository.createProduct(product)).thenReturn(99L);

        Long result = productService.createProduct(product);

        assertThat(result).isEqualTo(99L);
        verify(productRepository).createProduct(product);
        verifyNoInteractions(itemService);
    }

    @Test
    void createProductShouldPropagateRepositoryExceptions() {
        Product product = Product.builder()
                .id(1L)
                .name("Camera")
                .description("An amazing camera")
                .dailyPrice(12.5)
                .brand("BrandX")
                .model("ModelY")
                .categoryId(2L)
                .build();

        when(productRepository.createProduct(product)).thenThrow(new IllegalArgumentException("Invalid category"));

        assertThatThrownBy(() -> productService.createProduct(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid category");

        verify(productRepository).createProduct(product);
        verifyNoInteractions(itemService);
    }
}
