package com.iktpreobuka.project.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.management.relation.InvalidRoleValueException;
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

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.EUserRole;
import com.iktpreobuka.project.entities.VoucherEntity;
import com.iktpreobuka.project.entities.dto.VoucherDTO;
import com.iktpreobuka.project.repositories.VoucherRepository;
import com.iktpreobuka.project.security.Views;
import com.iktpreobuka.project.services.VoucherService;
import com.iktpreobuka.project.utils.RESTError;
import com.iktpreobuka.project.utils.Validation;

@RestController
@RequestMapping(path = "/project/vouchers")
public class VoucherController {

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private VoucherService voucherService;

	@GetMapping("/")
	public ResponseEntity<?> findAllVauchers() {
		try {
			return new ResponseEntity<>((List<VoucherEntity>) voucherRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/public")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> findAllVauchersPublic() {
		try {
			return new ResponseEntity<>((List<VoucherEntity>) voucherRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/admin")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> findAllVauchersAdmin() {
		try {
			return new ResponseEntity<>((List<VoucherEntity>) voucherRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/private")
	@JsonView(Views.Private.class)
	public ResponseEntity<?> findAllVauchersPrivate() {
		try {
			return new ResponseEntity<>((List<VoucherEntity>) voucherRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findVocherById(@PathVariable Integer id) {
		Optional<VoucherEntity> voucher = voucherRepository.findById(id);
		if (voucher.isPresent()) {
			return new ResponseEntity<>(voucher, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Voucher not found"),
				HttpStatus.NOT_FOUND);
	}

	@PostMapping("/{offerId}/buyer/{buyerId}")
	public ResponseEntity<?> addVoucher(@Valid @RequestBody VoucherDTO voucherDTO, @PathVariable Integer offerId,
			@PathVariable Integer buyerId, BindingResult result) throws InvalidRoleValueException {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Validation.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		VoucherEntity voucher = voucherService.addVoucher(voucherDTO, offerId, buyerId);

		return new ResponseEntity<>(voucher, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateVoucher(@PathVariable Integer id, @Valid @RequestBody VoucherEntity voucher,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Validation.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		VoucherEntity voucherDb = Validation.validateEntity(id, voucherRepository);

		voucherDb
				.setExpirationDate(Validation.setIfNotNull(voucherDb.getExpirationDate(), voucher.getExpirationDate()));
		voucherDb.setIsUsed(Validation.setIfNotNull(voucherDb.getIsUsed(), voucher.getIsUsed()));
		voucherDb.setVersion(Validation.setIfNotNull(voucherDb.getVersion(), voucher.getVersion()));
		voucherDb.setOffer(Validation.setIfNotNull(voucherDb.getOffer(), voucher.getOffer()));

		if (!voucher.getBuyer().equals(null) && voucher.getBuyer().getRole().equals(EUserRole.ROLE_CUSTOMER)) {
			voucherDb.setBuyer(voucher.getBuyer());
		}

		return new ResponseEntity<>(voucherRepository.save(voucherDb), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteVoucherById(@PathVariable Integer id) {
		Optional<VoucherEntity> voucher = voucherRepository.findById(id);
		if (voucher.isPresent()) {
			voucherRepository.delete(voucher.get());
			return new ResponseEntity<>(voucher, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Voucher not found"),
				HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByBuyer/{buyerId}")
	public ResponseEntity<?> findVoucherByBuyer(@PathVariable Integer buyerId) {

		try {
			return new ResponseEntity<>(voucherRepository.findByBuyerId(buyerId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/findByOffer/{offerId}")
	public ResponseEntity<?> findVoucherByOffer(@PathVariable Integer offerId) {

		try {
			return new ResponseEntity<>(voucherRepository.findByOfferId(offerId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/findNonExpiredVoucher")
	public ResponseEntity<?> findNonExpiredVoucher() {

		try {
			LocalDate currentDate = LocalDate.now();
			return new ResponseEntity<>(voucherRepository.findByExpirationDateAfter(currentDate), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
