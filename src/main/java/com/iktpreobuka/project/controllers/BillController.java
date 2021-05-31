package com.iktpreobuka.project.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.helpers.Validation;
import com.iktpreobuka.project.repositories.BillRepository;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;

@RestController
@RequestMapping(path = "/project/bills")
public class BillController {

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public List<BillEntity> findAllBills() {
		return (List<BillEntity>) billRepository.findAll();
	}

	@GetMapping("/{id}")
	public Optional<BillEntity> findBillById(@PathVariable Integer id) {
		return billRepository.findById(id);
	}

	@PostMapping("/{offerId}/buyer/{buyerId}")
	public BillEntity addBill(@RequestBody BillEntity bill, @PathVariable Integer offerId,
			@PathVariable Integer buyerId) {

		OfferEntity offer = Validation.validateEntity(offerId, offerRepository);
		UserEntity buyer = Validation.validateEntity(buyerId, userRepository);

		bill.setOffer(offer);
		bill.setBuyer(buyer);

		billRepository.save(bill);
		
		offer.setBoughtOffers(offer.getBoughtOffers() + 1);
		offer.setAvailableOffers(offer.getAvailableOffers()-1);
		
		offerRepository.save(offer);
		
		return bill;
	}

	@PutMapping("/{id}")
	public BillEntity updateBill(@PathVariable Integer id, @RequestBody BillEntity bill) {

		BillEntity billDb = Validation.validateEntity(id, billRepository);

		billDb.setBillCreated(Validation.setIfNotNull(billDb.getBillCreated(), bill.getBillCreated()));
		billDb.setBuyer(Validation.setIfNotNull(billDb.getBuyer(), bill.getBuyer()));
		billDb.setOffer(Validation.setIfNotNull(billDb.getOffer(), bill.getOffer()));
		billDb.setPaymentCanceled(Validation.setIfNotNull(billDb.getPaymentCanceled(), bill.getPaymentCanceled()));
		billDb.setPaymentMade(Validation.setIfNotNull(billDb.getPaymentMade(), bill.getPaymentMade()));
		billDb.setVersion(Validation.setIfNotNull(billDb.getVersion(), bill.getVersion()));

		billRepository.save(billDb);
		
		if(billDb.getPaymentCanceled()) {
			billDb.getOffer().setBoughtOffers(billDb.getOffer().getBoughtOffers() - 1);
			billDb.getOffer().setAvailableOffers(billDb.getOffer().getAvailableOffers()+1);
			
			offerRepository.save(billDb.getOffer());
		}
		
		return billDb;

	}

	@DeleteMapping("/{id}")
	public BillEntity deleteBillById(@PathVariable Integer id) {	
		BillEntity bill = Validation.validateEntity(id, billRepository);
		billRepository.delete(bill);
		return bill;
	}
	
	@GetMapping("/findByBuyer/{buyerId}")
	public List<BillEntity> findBillByBuyer(@PathVariable Integer buyerId) {
		return billRepository.findByBuyerId(buyerId);
	}
	
	@GetMapping("/findByCategory/{categoryId}")
	public List<BillEntity> findBillByCategory(@PathVariable Integer categoryId) {
		return billRepository.findByOfferCategoryId(categoryId);
	}
	
	@GetMapping("/findByDate/{startDate}/and/{endDate}")
	public List<BillEntity> findBillByBuyer(@PathVariable @DateTimeFormat(iso=ISO.DATE) LocalDate startDate, @PathVariable @DateTimeFormat(iso=ISO.DATE) LocalDate endDate) {
		return billRepository.findByBillCreatedBetween(startDate, endDate);
	}

}
