package com.iktpreobuka.project.services;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.dto.BillDTO;

public interface BillService {

	public BillEntity addBill(BillDTO bill, Integer offerId, Integer buyerId);

	public BillEntity updateBill(Integer id, BillEntity bill);

	public boolean areActiveBills(Integer categoryId);

	public void cancelBillsIfOfferExpired(Integer offerId);
}
