package edu.uoc.epcsd.productcatalog.domain.service;


import edu.uoc.epcsd.productcatalog.domain.Category;
import edu.uoc.epcsd.productcatalog.domain.repository.CategoryRepository;
import edu.uoc.epcsd.productcatalog.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAllCategories();
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findCategoryById(id);
    }

    @Override
    public List<Category> findCategoriesByExample(Category category) {
        return categoryRepository.findCategoriesByExample(category);
    }

    @Override
    public Long createCategory(Category category) {
        return categoryRepository.createCategory(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.updateCategory(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
    	
    	//check if category exists
    	Optional<Category> categoryOptional = categoryRepository.findCategoryById(id);
    	if (categoryOptional.isEmpty()) {
    		return false; // 404
    	}
    	
    	//business rule: cannot delete if it has subcategories
    	if (categoryRepository.existsByParentId(id)) {
    		throw new IllegalArgumentException("Cannot delete category with subcategories");
    	}

    	//business rule: cannot delete if it has assigned products
    	if (productRepository.existsByCategoryId(id)) {
    		throw new IllegalArgumentException("Cannot delete category with assigned products");
    	}
    	//deletes category
    	categoryRepository.deleteCategory(id);
    	
    	return true;
    }
    
}
