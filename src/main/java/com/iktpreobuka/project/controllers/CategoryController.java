package com.iktpreobuka.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.helpers.Validation;
import com.iktpreobuka.project.repositories.CategoryRepository;
import com.iktpreobuka.project.services.CategoryService;

@RestController
@RequestMapping(path = "/project/categories")
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/")
	public List<CategoryEntity> findAllCategories() {
		return (List<CategoryEntity>) categoryRepository.findAll();
	}

	@PostMapping("/")
	public CategoryEntity addCategory(@RequestBody CategoryEntity category) {
		return categoryRepository.save(category);
	}

	@PutMapping("/{id}")
	public CategoryEntity updateCategory(@PathVariable Integer id, @RequestBody CategoryEntity category) {
		
		CategoryEntity categoryDb = Validation.validateEntity(id, categoryRepository);

			categoryDb.setCategoryName(Validation.setIfNotNull(categoryDb.getCategoryName(), category.getCategoryName()));
			categoryDb.setCategoryDescription(Validation.setIfNotNull(categoryDb.getCategoryDescription(), category.getCategoryDescription()));
			
			return categoryRepository.save(categoryDb);

	}

	@GetMapping("/{id}")
	public Optional<CategoryEntity> findCategoryById(@PathVariable Integer id) {
		return categoryRepository.findById(id);

	}
	
	@DeleteMapping("/id")
	public CategoryEntity deleteCategory(@PathVariable Integer id) {
		return categoryService.deleteCategory(id);
	}

}
