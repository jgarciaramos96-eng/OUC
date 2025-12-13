package edu.uoc.epcsd.productcatalog.infrastructure.repository.jpa;


import edu.uoc.epcsd.productcatalog.domain.Category;
import edu.uoc.epcsd.productcatalog.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryRepositoryImpl implements CategoryRepository {

    private final SpringDataCategoryRepository jpaRepository;

    @Override
    public List<Category> findAllCategories() {
        return jpaRepository.findAll().stream().map(CategoryEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return jpaRepository.findById(id).map(CategoryEntity::toDomain);
    }

    public List<Category> findCategoriesByExample(Category category) {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", contains().ignoreCase())
                .withMatcher("description", contains().ignoreCase());


        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        if (category.getParentId() != null) {
            categoryEntity.setParent(jpaRepository.findById((category.getParentId())).orElseThrow(IllegalArgumentException::new));
        }

        Example<CategoryEntity> example = Example.of(categoryEntity, matcher);

        return jpaRepository.findAll(example).stream().map(CategoryEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Long createCategory(Category category) {

        CategoryEntity parentCategory = null;
        if (category.getParentId() != null) {
            parentCategory = jpaRepository.findById(category.getParentId()).orElseThrow(IllegalArgumentException::new);
        }

        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        categoryEntity.setParent(parentCategory);

        return jpaRepository.save(categoryEntity).getId();
    }

    @Override
    public void updateCategory(Category category) {
        CategoryEntity existingCategory = jpaRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (category.getParentId() != null) {
            CategoryEntity parentCategory = jpaRepository.findById(category.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            existingCategory.setParent(parentCategory);
        } else {
            existingCategory.setParent(null);
        }

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        jpaRepository.save(existingCategory);
    }
    
    @Override
    public void deleteCategory(Long id) {
    	Optional<CategoryEntity> category = jpaRepository.findById(id);
    	if (category.isEmpty()) {
    		throw new IllegalArgumentException("Category does not exist");
    	}
    	jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByParentId(Long parentId) {
    	return jpaRepository.existsByParentId(parentId);
    }
}
