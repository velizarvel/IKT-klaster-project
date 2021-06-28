package com.iktpreobuka.project.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.dto.BillDTO;
import com.iktpreobuka.project.repositories.BillRepository;
import com.iktpreobuka.project.security.Views;
import com.iktpreobuka.project.services.BillService;
import com.iktpreobuka.project.utils.RESTError;
import com.iktpreobuka.project.utils.Validation;

@RestController
@RequestMapping(path = "/project/bills")
public class BillController {

	@Autowired
	private BillService billService;

	@Autowired
	private BillRepository billRepository;

	@GetMapping("/")
	public ResponseEntity<?> findAllBills() {
		try {
			return new ResponseEntity<>((List<BillEntity>) billRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/public")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> findAllBillsPublic() {
		try {
			return new ResponseEntity<>((List<BillEntity>) billRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/admin")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> findAllBillsAdmin() {
		try {
			return new ResponseEntity<>((List<BillEntity>) billRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/private")
	@JsonView(Views.Private.class)
	public ResponseEntity<?> findAllBillsPrivate() {
		try {
			return new ResponseEntity<>((List<BillEntity>) billRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findBillById(@PathVariable Integer id) {
		Optional<BillEntity> bill = billRepository.findById(id);
		if (bill.isPresent()) {
			return new ResponseEntity<>(bill, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Bill not found"),
				HttpStatus.NOT_FOUND);
	}

	@PostMapping("/{offerId}/buyer/{buyerId}")
	public ResponseEntity<?> addBill(@Valid @RequestBody BillDTO billDTO, @PathVariable Integer offerId,
			@PathVariable Integer buyerId, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Validation.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		BillEntity bill = billService.addBill(billDTO, offerId, buyerId);

		return new ResponseEntity<>(bill, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateBill(@PathVariable Integer id, @Valid @RequestBody BillEntity bill,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Validation.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(billService.updateBill(id, bill), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBillById(@PathVariable Integer id) {
		Optional<BillEntity> bill = billRepository.findById(id);

		if (bill.isPresent()) {
			billRepository.delete(bill.get());
			return new ResponseEntity<>(bill, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Bill not found"),
				HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByBuyer/{buyerId}")
	public ResponseEntity<?> findBillByBuyer(@PathVariable Integer buyerId) {

		try {
			return new ResponseEntity<>(billRepository.findByBuyerId(buyerId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/findByCategory/{categoryId}")
	public ResponseEntity<?> findBillByCategory(@PathVariable Integer categoryId) {
		try {
			return new ResponseEntity<>(billRepository.findByOfferCategoryId(categoryId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/findByDate/{startDate}/and/{endDate}")
	public ResponseEntity<?> findBillByBuyer(@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {

		try {
			return new ResponseEntity<>(billRepository.findByBillCreatedBetween(startDate, endDate), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
