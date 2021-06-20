package com.iktpreobuka.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private OfferService offerService;
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public CategoryEntity deleteCategory(Integer id) {
		
		CategoryEntity category = categoryRepository.findById(id).orElse(null);
		
		boolean checkOfers = offerService.hasCategoryNonExpirationOffers(id);
		boolean checkBills = billService.areActiveBills(id);
		
		if(category==null || checkOfers || checkBills) {
			return null;
		}
		
		categoryRepository.delete(category);
		
		return category;
		
	}

	
	
}
