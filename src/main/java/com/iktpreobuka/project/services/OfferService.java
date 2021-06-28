package com.iktpreobuka.project.services;

import java.util.Optional;

import com.iktpreobuka.project.entities.EOfferStatus;
import com.iktpreobuka.project.entities.OfferEntity;

public interface OfferService {

	public void setNumberOfBoughtAndAvailableOffersByPaymentCanceled(Integer id, boolean paymentCanceled);

	public void setNumberOfBoughtAndAvailableOffersByPaymentMade(Integer id, boolean paymentMade);

	public boolean hasCategoryNonExpirationOffers(Integer categoryId);

	public Optional<OfferEntity> changeOfferStatus(Integer id, EOfferStatus status);
}
