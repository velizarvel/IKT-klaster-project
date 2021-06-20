package com.iktpreobuka.project.services;

import com.iktpreobuka.project.entities.VoucherEntity;

public interface EmailService {

	public void sendTemplateMessage(VoucherEntity voucher) throws Exception;

}
