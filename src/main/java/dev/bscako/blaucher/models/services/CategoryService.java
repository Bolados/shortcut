/**
 *
 */
package dev.bscako.blaucher.models.services;

import dev.bscako.blaucher.models.Application;
import dev.bscako.blaucher.models.Category;
import dev.bscako.blaucher.models.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BSCAKO
 *
 */

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * @return the categoryRepository
     */
    public CategoryRepository getCategoryRepository() {
        return this.categoryRepository;
    }

    public List<Category> findAllCategories() {
        return this.categoryRepository.findAll().stream().distinct().collect(Collectors.toList());
    }

    public Category addCategory(String name, List<Application> applications) {

        Category category = new Category();
        category.setName(name);
        category.setApplications(new ArrayList<>());

        return this.categoryRepository.save(category);
    }


}
