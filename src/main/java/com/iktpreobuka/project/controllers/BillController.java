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
import com.iktpreobuka.project.helpers.Validation;
import com.iktpreobuka.project.repositories.BillRepository;
import com.iktpreobuka.project.services.BillService;
import com.iktpreobuka.project.services.VoucherService;

@RestController
@RequestMapping(path = "/project/bills")
public class BillController {

	@Autowired
	private BillService billService;

	@Autowired
	private BillRepository billRepository;

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

		return billService.addBill(bill, offerId, buyerId);
	}

	@PutMapping("/{id}")
	public BillEntity updateBill(@PathVariable Integer id, @RequestBody BillEntity bill) {
		return billService.updateBill(id, bill);

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
	public List<BillEntity> findBillByBuyer(@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
		return billRepository.findByBillCreatedBetween(startDate, endDate);
	}

}
