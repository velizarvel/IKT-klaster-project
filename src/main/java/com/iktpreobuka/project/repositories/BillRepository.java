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
	
	public List<BillEntity> findByBillCreatedAndPaymentMade(LocalDate date, Boolean paymentMade);
	
	public List<BillEntity> findByBillCreatedAndPaymentMadeAndOfferCategoryId(LocalDate date, Boolean paymentMade, Integer categoryId);
	
	public List<BillEntity> findByPaymentMadeAndBillCreatedBetween(Boolean paymentMade, LocalDate startDate, LocalDate endDate);
	
	public List<BillEntity> findByOfferCategoryIdAndPaymentMadeAndBillCreatedBetween(Integer categoryId, Boolean paymentMade, LocalDate startDate, LocalDate endDate);
}
