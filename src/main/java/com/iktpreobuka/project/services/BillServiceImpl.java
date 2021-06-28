package com.iktpreobuka.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.entities.dto.BillDTO;
import com.iktpreobuka.project.repositories.BillRepository;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.utils.Validation;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	BillRepository billRepository;

	@Autowired
	OfferRepository offerRepository;

	@Autowired
	OfferService offerService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	VoucherService voucherService;

	@Override
	public BillEntity addBill(BillDTO billDTO, Integer offerId, Integer buyerId) {

		OfferEntity offer = Validation.validateEntity(offerId, offerRepository);
		UserEntity buyer = Validation.validateEntity(buyerId, userRepository);

		BillEntity bill = new BillEntity(billDTO.getPaymentMade(), billDTO.getPaymentCanceled(),
				billDTO.getBillCreated(), offer, buyer);

		billRepository.save(bill);

		offerService.setNumberOfBoughtAndAvailableOffersByPaymentMade(offer.getId(), bill.getPaymentMade());

		offerRepository.save(offer);

		voucherService.createVoucherWhenPaymentMade(bill);

		return bill;
	}

	public BillEntity updateBill(Integer id, BillEntity bill) {

		BillEntity billDb = Validation.validateEntity(id, billRepository);

		billDb.setBillCreated(Validation.setIfNotNull(billDb.getBillCreated(), bill.getBillCreated()));
		billDb.setBuyer(Validation.setIfNotNull(billDb.getBuyer(), bill.getBuyer()));
		billDb.setOffer(Validation.setIfNotNull(billDb.getOffer(), bill.getOffer()));
		billDb.setPaymentCanceled(Validation.setIfNotNull(billDb.getPaymentCanceled(), bill.getPaymentCanceled()));
		billDb.setPaymentMade(Validation.setIfNotNull(billDb.getPaymentMade(), bill.getPaymentMade()));
		billDb.setVersion(Validation.setIfNotNull(billDb.getVersion(), bill.getVersion()));

		billRepository.save(billDb);

		offerService.setNumberOfBoughtAndAvailableOffersByPaymentCanceled(bill.getOffer().getId(),
				bill.getPaymentCanceled());
		offerService.setNumberOfBoughtAndAvailableOffersByPaymentMade(id, bill.getPaymentMade());

		offerRepository.save(billDb.getOffer());

		voucherService.createVoucherWhenPaymentMade(billDb);

		return billDb;

	}

	@Override
	public boolean areActiveBills(Integer categoryId) {
		List<BillEntity> bills = billRepository.findByOfferCategoryId(categoryId);
		for (BillEntity bill : bills) {
			if (!bill.getPaymentMade() && !bill.getPaymentCanceled()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void cancelBillsIfOfferExpired(Integer offerId) {

		List<BillEntity> bills = billRepository.findByOfferId(offerId);
		bills.stream().forEach(b -> {
			b.setPaymentCanceled(true);
			billRepository.save(b);
		});
	}

}
