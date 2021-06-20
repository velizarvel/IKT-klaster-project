package com.iktpreobuka.project.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.project.entities.BillEntity;

public interface BillRepository extends CrudRepository<BillEntity, Integer>{

	public List<BillEntity> findByBuyerId(Integer buyerId);
	
	public List<BillEntity> findByOfferCategoryId(Integer categoryId);
	
	public List<BillEntity> findByBillCreatedBetween(LocalDate startDate, LocalDate endDate);
	
	public List<BillEntity> findByOfferId(Integer offerId);
}
