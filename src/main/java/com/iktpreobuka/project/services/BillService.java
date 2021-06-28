package com.iktpreobuka.project.services;

import java.time.LocalDate;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.entities.dto.BillDTO;
import com.iktpreobuka.project.entities.dto.ReportDTO;

public interface BillService {

	public BillEntity addBill(BillDTO bill, Integer offerId, Integer buyerId);

	public BillEntity updateBill(Integer id, BillEntity bill);

	public boolean areActiveBills(Integer categoryId);

	public void cancelBillsIfOfferExpired(Integer offerId);

	public ReportDTO generateReportByDate(LocalDate startDate, LocalDate endDate);

	public ReportDTO generateReportByDateAndCategory(LocalDate startDate, LocalDate endDate, CategoryEntity category);
}
