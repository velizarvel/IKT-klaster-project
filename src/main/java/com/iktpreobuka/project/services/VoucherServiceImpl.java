package com.iktpreobuka.project.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.VoucherEntity;
import com.iktpreobuka.project.repositories.VoucherRepository;

@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	EmailService emailService;

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

}
