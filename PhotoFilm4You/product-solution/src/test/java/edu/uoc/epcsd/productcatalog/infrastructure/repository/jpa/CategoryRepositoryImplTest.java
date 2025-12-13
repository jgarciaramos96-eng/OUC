package edu.uoc.epcsd.productcatalog.infrastructure.repository.jpa;

import edu.uoc.epcsd.productcatalog.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryRepositoryImplTest {

    private SpringDataCategoryRepository jpaRepository;
    private CategoryRepositoryImpl categoryRepository;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(SpringDataCategoryRepository.class);
        categoryRepository = new CategoryRepositoryImpl(jpaRepository);
    }


    @Test
    void updateCategory_shouldThrowException_whenCategoryNotFound() {

        Category category = Category.builder()
                .id(1L)
                .name("Name")
                .description("Desc")
                .parentId(null)
                .build();

        when(jpaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> categoryRepository.updateCategory(category));

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void updateCategory_shouldUpdateParent_whenParentIdNotNull() {

        Category category = Category.builder()
                .id(1L)
                .name("Updated Name")
                .description("Updated Desc")
                .parentId(10L)
                .build();

        CategoryEntity existing = new CategoryEntity();
        existing.setId(1L);

        CategoryEntity parent = new CategoryEntity();
        parent.setId(10L);

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(jpaRepository.findById(10L)).thenReturn(Optional.of(parent));

        categoryRepository.updateCategory(category);

        ArgumentCaptor<CategoryEntity> captor = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(jpaRepository).save(captor.capture());

        CategoryEntity saved = captor.getValue();

        assertEquals("Updated Name", saved.getName());
        assertEquals("Updated Desc", saved.getDescription());
        assertEquals(parent, saved.getParent());
    }

    @Test
    void updateCategory_shouldRemoveParent_whenParentIdIsNull() {

        Category category = Category.builder()
                .id(1L)
                .name("Updated Name")
                .description("Updated Desc")
                .parentId(null)
                .build();

        CategoryEntity existing = new CategoryEntity();
        existing.setId(1L);

        CategoryEntity oldParent = new CategoryEntity();
        oldParent.setId(99L);
        existing.setParent(oldParent);

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(existing));

        categoryRepository.updateCategory(category);

        ArgumentCaptor<CategoryEntity> captor = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(jpaRepository).save(captor.capture());

        CategoryEntity saved = captor.getValue();

        assertNull(saved.getParent());
        assertEquals("Updated Name", saved.getName());
        assertEquals("Updated Desc", saved.getDescription());
    }
    
    @Test
    void deleteCategory_shouldCallJpaDeleteWhenCategoryExists() {
    	CategoryEntity existing = new CategoryEntity();
    	existing.setId(1L);
    	
    	when(jpaRepository.findById(1L)).thenReturn(Optional.of(existing));
    	
    	categoryRepository.deleteCategory(1L);
    	
    	verify(jpaRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void deleteCategory_shouldThrowExceptionWhenCategoryDoesNotExist() {
    	when(jpaRepository.findById(2L)).thenReturn(Optional.empty());
    	
    	assertThrows(IllegalArgumentException.class,
    			() -> categoryRepository.deleteCategory(2L));
    	
    	verify(jpaRepository, never()).deleteById(any());
    }
    
    @Test
    void existsByParentId_shouldReturnTrueWhenSubcategoriesExist() {
    	when(jpaRepository.existsByParentId(3L)).thenReturn(true);
    	
    	boolean result = categoryRepository.existsByParentId(3L);
    	
    	assertTrue(result, "Should return true when subcategories exists for the category");
    	verify(jpaRepository, times(1)).existsByParentId(3L);
    }
    
    @Test
    void existsByParentId_shouldReturnFalseWhenNoSubcategoriesExist() {
    	when(jpaRepository.existsByParentId(4L)).thenReturn(false);
    	
    	boolean result = categoryRepository.existsByParentId(4L);
    	
    	assertFalse(result, "Should return false when no subcategories exists for the category");
    	verify(jpaRepository, times(1)).existsByParentId(4L);
    }
}
