package edu.uoc.epcsd.productcatalog.infrastructure.repository.jpa;

import edu.uoc.epcsd.productcatalog.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {

    @Mock
    private SpringDataProductRepository jpaRepository;

    @Mock
    private SpringDataCategoryRepository jpaCategoryRepository;

    @InjectMocks
    private ProductRepositoryImpl productRepositoryImpl;

    private Product product;

    @BeforeEach
    void setup() {
        // Product con categoryId válido
        product = Product.builder()
                .id(1L)
                .name("Camera")
                .description("Professional camera")
                .dailyPrice(50.0)
                .brand("Canon")
                .model("X-Pro2000")
                .categoryId(10L)
                .build();
    }

    @Test
    void updateProduct_categoryFound() {
        when(jpaCategoryRepository.findById(10L)).thenReturn(Optional.of(new CategoryEntity()));
        when(jpaRepository.save(any(ProductEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> productRepositoryImpl.updateProduct(product));

        verify(jpaCategoryRepository, times(1)).findById(10L);
        verify(jpaRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void updateProduct_categoryNotFound() {
        when(jpaCategoryRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productRepositoryImpl.updateProduct(product));

        verify(jpaCategoryRepository, times(1)).findById(10L);
        verify(jpaRepository, never()).save(any());
    }
    
    @Test
    void existsByCategoryId_shouldReturnTrueWhenProductsExist() {
    	when(jpaRepository.existsByCategoryId(1L)).thenReturn(true);
    	
    	boolean result = productRepositoryImpl.existsByCategoryId(1L);
    	
    	assertTrue(result, "Should return true when products exists for the category");
    	verify(jpaRepository, times(1)).existsByCategoryId(1L);
    }
    
    @Test
    void existsByCategoryId_shouldReturnFalseWhenNoProductsExist() {
    	when(jpaRepository.existsByCategoryId(2L)).thenReturn(false);
    	
    	boolean result = productRepositoryImpl.existsByCategoryId(2L);
    	
    	assertFalse(result, "Should return false when no products exists for the category");
    	verify(jpaRepository, times(1)).existsByCategoryId(2L);
    }
}
