package com.iktpreobuka.project.services;

import javax.management.relation.InvalidRoleValueException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.VoucherEntity;
import com.iktpreobuka.project.entities.dto.VoucherDTO;

public interface VoucherService {
	
	public void createVoucherWhenPaymentMade(BillEntity bill);
	
	public VoucherEntity addVoucher(VoucherDTO voucherDTO, Integer offerId,
			Integer buyerId) throws InvalidRoleValueException;

}
