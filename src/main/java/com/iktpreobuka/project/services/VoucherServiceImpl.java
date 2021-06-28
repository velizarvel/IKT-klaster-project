package com.iktpreobuka.project.services;

import java.time.LocalDate;

import javax.management.relation.InvalidRoleValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.EUserRole;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.entities.VoucherEntity;
import com.iktpreobuka.project.entities.dto.VoucherDTO;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.repositories.VoucherRepository;
import com.iktpreobuka.project.utils.Validation;

@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	OfferRepository offerRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public void createVoucherWhenPaymentMade(BillEntity bill) {
		if (bill.getPaymentMade()) {
			VoucherEntity voucher = new VoucherEntity(LocalDate.now().plusDays(10), false, bill.getOffer(),
					bill.getBuyer());
			voucherRepository.save(voucher);
			try {
				emailService.sendTemplateMessage(voucher);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public VoucherEntity addVoucher(VoucherDTO voucherDTO, Integer offerId, Integer buyerId)
			throws InvalidRoleValueException {

		OfferEntity offer = Validation.validateEntity(offerId, offerRepository);
		UserEntity buyer = Validation.validateEntity(buyerId, userRepository);

		if (!buyer.getRole().equals(EUserRole.ROLE_CUSTOMER)) {
			throw new InvalidRoleValueException("The customer doesn't have property role for adding voucher.");
		}

		VoucherEntity voucher = new VoucherEntity(voucherDTO.getExpirationDate(), voucherDTO.getIsUsed(), offer, buyer);

		voucherRepository.save(voucher);

		return voucher;
	}

}
