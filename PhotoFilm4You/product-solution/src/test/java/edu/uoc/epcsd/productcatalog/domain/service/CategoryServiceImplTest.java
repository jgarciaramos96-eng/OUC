package edu.uoc.epcsd.productcatalog.domain.service;

import edu.uoc.epcsd.productcatalog.domain.Category;
import edu.uoc.epcsd.productcatalog.domain.repository.CategoryRepository;
import edu.uoc.epcsd.productcatalog.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceImplTest {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        productRepository = mock(ProductRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository,productRepository);
    }

    @Test
    void updateCategory_shouldDelegateCallToRepository() {

        Category category = Category.builder()
                .id(1L)
                .name("Test")
                .description("Desc")
                .parentId(null)
                .build();

        categoryService.updateCategory(category);

        verify(categoryRepository, times(1)).updateCategory(category);
    }

    @Test
    void updateCategory_shouldPropagateException() {

        Category category = Category.builder()
                .id(1L)
                .name("Test")
                .description("Desc")
                .parentId(null)
                .build();

        doThrow(new IllegalArgumentException("Error")).when(categoryRepository).updateCategory(category);

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.updateCategory(category));
    }
    
    @Test
    void deleteCategory_shouldReturnFalseWhenCategoryDoesNotExist() {
    	when(categoryRepository.findCategoryById(1L)).thenReturn(Optional.empty());
    	
    	boolean result = categoryService.deleteCategory(1L);
    	assertFalse(result, "Delete should return false for non-existent categories");
    }
    
    @Test
    void deleteCategory_shouldThrowExceptionWhenSubcategoriesExist() {
    	when(categoryRepository.findCategoryById(2L)).thenReturn(Optional.of(Category.builder().id(2L).build()));
    	when(categoryRepository.existsByParentId(2L)).thenReturn(true);
    	
    	IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
    			() -> categoryService.deleteCategory(2L));
    	
    	assertEquals("Cannot delete category with subcategories", exception.getMessage());
    }
    
    @Test
    void deleteCategory_shouldThrowExceptionWhenProductExists() {
    	when(categoryRepository.findCategoryById(3L)).thenReturn(Optional.of(Category.builder().id(3L).build()));
    	when(productRepository.existsByCategoryId(3L)).thenReturn(true);
    	
    	IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
    			() -> categoryService.deleteCategory(3L));
    	
    	assertEquals("Cannot delete category with assigned products", exception.getMessage());
    }
    
    @Test
    void deleteCategory_shouldReturnTrueWhenCategoryIsDeletable() {
    	when(categoryRepository.findCategoryById(4L)).thenReturn(Optional.of(Category.builder().id(4L).build()));
    	when(categoryRepository.existsByParentId(4L)).thenReturn(false);
    	when(productRepository.existsByCategoryId(4L)).thenReturn(false);
    	
    	boolean result = categoryService.deleteCategory(4L);
    	assertTrue(result, "Delete should return true for deletable category");
    	verify(categoryRepository, times(1)).deleteCategory(4L);
    }
}
