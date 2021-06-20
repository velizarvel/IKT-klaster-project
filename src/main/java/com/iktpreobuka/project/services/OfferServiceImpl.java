package com.iktpreobuka.project.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.project.entities.EOfferStatus;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.repositories.OfferRepository;

@Service
public class OfferServiceImpl implements OfferService {

	@Autowired
	OfferRepository offerRepository;

	@Autowired
	BillService billService;

	@Override
	public void setNumberOfBoughtAndAvailableOffersByPaymentCanceled(Integer id, boolean paymentCanceled) {
		OfferEntity offer = offerRepository.findById(id).orElse(null);
		if (offer == null) {
			return;
		}

		if (paymentCanceled) {
			offer.setBoughtOffers(offer.getBoughtOffers() - 1);
			offer.setAvailableOffers(offer.getAvailableOffers() + 1);
		}
	}

	@Override
	public void setNumberOfBoughtAndAvailableOffersByPaymentMade(Integer id, boolean paymentMade) {
		OfferEntity offer = offerRepository.findById(id).orElse(null);
		if (offer == null) {
			return;
		}
		if (paymentMade) {
			offer.setBoughtOffers(offer.getBoughtOffers() + 1);
			offer.setAvailableOffers(offer.getAvailableOffers() - 1);
		}
	}

	@Override
	public boolean hasCategoryNonExpirationOffers(Integer categoryId) {
		List<OfferEntity> offers = offerRepository.findAllByCategoryId(categoryId);
		LocalDate currentDate = LocalDate.now();
		for (OfferEntity offer : offers) {
			if (offer.getOfferExpires().isAfter(currentDate)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Optional<OfferEntity> changeOfferStatus(Integer id, EOfferStatus status) {
		Optional<OfferEntity> offer = offerRepository.findById(id);
		if (offer.isPresent()) {
			offer.get().setOfferStatus(status);
			offerRepository.save(offer.get());
			if (status.equals(EOfferStatus.EXPIRED)) {
				billService.cancelBillsIfOfferExpired(offer.get().getId());
			}
		}

		return offer;
	}
}
