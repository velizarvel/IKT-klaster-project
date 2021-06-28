package com.iktpreobuka.project.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.project.entities.BillEntity;
import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.entities.dto.BillDTO;
import com.iktpreobuka.project.entities.dto.ReportDTO;
import com.iktpreobuka.project.entities.dto.ReportItemDTO;
import com.iktpreobuka.project.repositories.BillRepository;
import com.iktpreobuka.project.repositories.CategoryRepository;
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

	@Autowired
	CategoryRepository categoryRepository;

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

	@Override
	public ReportDTO generateReportByDate(LocalDate startDate, LocalDate endDate) {

		ReportDTO report = new ReportDTO();
		List<BillEntity> billsByPeriod = billRepository.findByBillCreatedBetween(startDate, endDate);

		report.setCategoryName("Report from " + startDate + " to " + endDate);

		Double sumOfIncomes = billsByPeriod.stream().map(b -> b.getOffer().getRegularPrice())
				.collect(Collectors.summingDouble(Double::doubleValue));
		report.setSumOfIncomes(sumOfIncomes);

		report.setTotalNumberOfSoldOffers(billsByPeriod.size());

		report.setReportItems(getListOfReportItem(startDate, endDate, null));

		return report;

	}

	@Override
	public ReportDTO generateReportByDateAndCategory(LocalDate startDate, LocalDate endDate, CategoryEntity category) {

		ReportDTO report = new ReportDTO();
		List<BillEntity> bills = billRepository
				.findByOfferCategoryIdAndPaymentMadeAndBillCreatedBetween(category.getId(), true, startDate, endDate);

		report.setCategoryName(category.getCategoryName());

		report.setTotalNumberOfSoldOffers(bills.size());

		Double sumOfIncomes = bills.stream().map(b -> b.getOffer().getRegularPrice())
				.collect(Collectors.summingDouble(Double::doubleValue));

		report.setSumOfIncomes(sumOfIncomes);

		report.setReportItems(getListOfReportItem(startDate, endDate, category));

		return report;

	}

	private List<ReportItemDTO> getListOfReportItem(LocalDate startDate, LocalDate endDate, CategoryEntity category) {
		List<ReportItemDTO> reportItems = new ArrayList<>();
		List<BillEntity> billsByDate = null;

		for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {

			if (category != null) {
				billsByDate = billRepository.findByBillCreatedAndPaymentMadeAndOfferCategoryId(date, true,
						category.getId());
			} else {
				billsByDate = billRepository.findByBillCreatedAndPaymentMade(date, true);
			}

			if (billsByDate.size() > 0) {
				ReportItemDTO reportItem = new ReportItemDTO();
				reportItem.setDate(date);

				reportItem.setNumberOfOffers(billsByDate.size());

				Double income = billsByDate.stream().map(b -> b.getOffer().getRegularPrice())
						.collect(Collectors.summingDouble(Double::doubleValue));
				reportItem.setIncome(income);

				reportItems.add(reportItem);
			}
		}
		return reportItems;
	}

}
