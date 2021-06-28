package com.iktpreobuka.project.services;

import javax.management.relation.InvalidRoleValueException;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.VoucherEntity;
import com.iktpreobuka.project.entities.dto.VoucherDTO;

public interface VoucherService {
	
	public void createVoucherWhenPaymentMade(BillEntity bill);
	
	public VoucherEntity addVoucher(VoucherDTO voucherDTO, Integer offerId,
			Integer buyerId) throws InvalidRoleValueException;

}
