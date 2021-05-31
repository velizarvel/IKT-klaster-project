package com.iktpreobuka.project.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.management.relation.InvalidRoleValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.EUserRole;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.entities.VoucherEntity;
import com.iktpreobuka.project.helpers.Validation;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.repositories.VoucherRepository;

@RestController
@RequestMapping(path = "/project/vouchers")
public class VoucherController {

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public List<VoucherEntity> findAllVauchers() {
		return (List<VoucherEntity>) voucherRepository.findAll();
	}

	@GetMapping("/{id}")
	public Optional<VoucherEntity> findVocherById(@PathVariable Integer id) {
		return voucherRepository.findById(id);
	}

	@PostMapping("/{offerId}/buyer/{buyerId}")
	public VoucherEntity addVoucher(@RequestBody VoucherEntity voucher, @PathVariable Integer offerId,
			@PathVariable Integer buyerId) throws InvalidRoleValueException {

		OfferEntity offer = Validation.validateEntity(offerId, offerRepository);
		UserEntity buyer = Validation.validateEntity(buyerId, userRepository);
		
		if (!buyer.getRole().equals(EUserRole.ROLE_CUSTOMER)) {
			throw new InvalidRoleValueException("The customer doesn't have property role for adding voucher.");
		}

		voucher.setOffer(offer);
		voucher.setBuyer(buyer);

		return voucherRepository.save(voucher);
	}

	@PutMapping("/{id}")
	public VoucherEntity updateVoucher(@PathVariable Integer id, @RequestBody VoucherEntity voucher){

		VoucherEntity voucherDb = Validation.validateEntity(id, voucherRepository);

		voucherDb.setExpirationDate(Validation.setIfNotNull(voucherDb.getExpirationDate(), voucher.getExpirationDate()));
		voucherDb.setIsUsed(Validation.setIfNotNull(voucherDb.getIsUsed(), voucher.getIsUsed()));
		voucherDb.setVersion(Validation.setIfNotNull(voucherDb.getVersion(), voucher.getVersion()));
		voucherDb.setOffer(Validation.setIfNotNull(voucherDb.getOffer(), voucher.getOffer()));
		
		if (!voucher.getBuyer().equals(null) && voucher.getBuyer().getRole().equals(EUserRole.ROLE_CUSTOMER)) {
			voucherDb.setBuyer(voucher.getBuyer());
		}

		return voucherRepository.save(voucherDb);

	}

	@DeleteMapping("/{id}")
	public VoucherEntity deleteVoucherById(@PathVariable Integer id) {	
		VoucherEntity voucher = Validation.validateEntity(id, voucherRepository);
		voucherRepository.delete(voucher);
		return voucher;
	}
	
	@GetMapping("/findByBuyer/{buyerId}")
	public List<VoucherEntity> findVoucherByBuyer(@PathVariable Integer buyerId) {
		return voucherRepository.findByBuyerId(buyerId);
	}
	
	@GetMapping("/findByOffer/{offerId}")
	public List<VoucherEntity> findVoucherByOffer(@PathVariable Integer offerId) {
		return voucherRepository.findByOfferId(offerId);
	}
	
	@GetMapping("/findNonExpiredVoucher")
	public List<VoucherEntity> findNonExpiredVoucher() {
		
		LocalDate currentDate = LocalDate.now();
		
		return voucherRepository.findByExpirationDateAfter(currentDate);
	}

}
