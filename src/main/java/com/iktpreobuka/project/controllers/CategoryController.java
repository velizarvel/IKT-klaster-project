package com.iktpreobuka.project.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.repositories.CategoryRepository;
import com.iktpreobuka.project.services.CategoryService;
import com.iktpreobuka.project.utils.RESTError;
import com.iktpreobuka.project.utils.Validation;

@RestController
@RequestMapping(path = "/project/categories")
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/")
	public ResponseEntity<?> findAllCategories() {
		try {
			return new ResponseEntity<>((List<CategoryEntity>) categoryRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/")
	public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryEntity category, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(new RESTError(HttpStatus.BAD_REQUEST.value(), result.toString()),
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryEntity category,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(new RESTError(HttpStatus.BAD_REQUEST.value(), result.toString()),
					HttpStatus.BAD_REQUEST);
		}

		CategoryEntity categoryDb = Validation.validateEntity(id, categoryRepository);

		categoryDb.setCategoryName(Validation.setIfNotNull(categoryDb.getCategoryName(), category.getCategoryName()));
		categoryDb.setCategoryDescription(
				Validation.setIfNotNull(categoryDb.getCategoryDescription(), category.getCategoryDescription()));

		return new ResponseEntity<>(categoryRepository.save(categoryDb), HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findCategoryById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<>(categoryRepository.findById(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Category not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/id")
	public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
		try {
			return new ResponseEntity<>(categoryService.deleteCategory(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Category not found"),
					HttpStatus.NOT_FOUND);
		}
	}

}
