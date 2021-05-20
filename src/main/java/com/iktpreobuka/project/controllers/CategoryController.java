package com.iktpreobuka.project.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.CategoryEntity;

@RestController
@RequestMapping(path = "/project/categories")
public class CategoryController {

	List<CategoryEntity> categories = new ArrayList<CategoryEntity>();

	private List<CategoryEntity> getDB() {
		if (categories.size() == 0) {
			CategoryEntity c1 = new CategoryEntity(1, "music", "description 1");
			CategoryEntity c2 = new CategoryEntity(2, "food", "description 2");
			CategoryEntity c3 = new CategoryEntity(3, "entertainment", "description 3");
			categories.add(c1);
			categories.add(c2);
			categories.add(c3);
		}

		return categories;
	}

	@GetMapping("/")
	public List<CategoryEntity> findAllCategories() {
		return getDB();
	}
	
	@PostMapping("/")
	public CategoryEntity addCategory(@RequestBody CategoryEntity category) {
		Integer id = Math.abs(new Random().nextInt());
		CategoryEntity newCategory = new CategoryEntity(id, category.getCategoryName(), category.getCategoryDescription());
		if(!category.equals(null)) {
			categories.add(newCategory);
			return newCategory;
		}
		return null;
	}
	
	@PutMapping("/{id}")
	public CategoryEntity updateCategory(@PathVariable Integer id, @RequestBody CategoryEntity category) {
		CategoryEntity categoryDb = findCategoryById(id);
		if(!category.equals(null)) {
			categoryDb.setCategoryName(category.getCategoryName());
			categoryDb.setCategoryDescription(category.getCategoryDescription());
		}
		return null;
	}
	
	@GetMapping("/{id}")
	public CategoryEntity findCategoryById(@PathVariable Integer id) {
		try {
			return getDB().stream().filter(x -> x.getId().equals(id)).findFirst().get();
		} catch (Exception e) {
			return null;
		}
		
	}

}
