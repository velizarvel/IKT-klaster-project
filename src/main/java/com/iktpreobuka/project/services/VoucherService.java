package com.iktpreobuka.project.services;

import com.iktpreobuka.project.entities.BillEntity;

public interface VoucherService {
	
	public void createVoucherWhenPaymentMade(BillEntity bill);

}
