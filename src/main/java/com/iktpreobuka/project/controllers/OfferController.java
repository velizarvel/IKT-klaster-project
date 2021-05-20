package com.iktpreobuka.project.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.EOfferStatus;
import com.iktpreobuka.project.entities.OfferEntity;

@RestController
@RequestMapping(path = "/project/offers")
public class OfferController {

	private List<OfferEntity> offers = new ArrayList<>();

	private List<OfferEntity> getDB() {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 5);

		if (offers.size() == 0) {
			OfferEntity o1 = new OfferEntity(1, "2 tickets for Killers concert", "Enjoy!!!", new Date(), cal.getTime(),
					100000.00, 6500.00, " ", 10, 0, EOfferStatus.WAIT_FOR_APPROVING);
			OfferEntity o2 = new OfferEntity(2, "VIVAX 24LE76T2", "Don't miss this fantastic offer!", new Date(),
					cal.getTime(), 200000.00, 16500.00, " ", 5, 0, EOfferStatus.WAIT_FOR_APPROVING);
			OfferEntity o3 = new OfferEntity(3, "Dinner for two in Aqua Doria", "Excellent offer", new Date(),
					cal.getTime(), 6000.00, 3500.00, " ", 4, 0, EOfferStatus.WAIT_FOR_APPROVING);
			offers.add(o1);
			offers.add(o2);
			offers.add(o3);
		}
		return offers;
	}

	@GetMapping("/")
	public List<OfferEntity> findAllOffers() {
		return getDB();
	}

	@PostMapping("/")
	public OfferEntity addOffer(@RequestBody OfferEntity offer) {
		Integer id = Math.abs(new Random().nextInt());
		OfferEntity newOffer = new OfferEntity(id, offer.getOfferName(), offer.getOfferDescription(),
				offer.getOfferCreated(), offer.getOfferCreated(), offer.getRegularPrice(), offer.getActionPrice(),
				offer.getImagePath(), offer.getAvailableOffers(), offer.getBoughtOffers(), offer.getOfferStatus());
		if (!offer.equals(null)) {
			offers.add(newOffer);
			return newOffer;
		}
		return null;
	}

	@PutMapping("/{id}")
	public OfferEntity updateOffer(@RequestBody OfferEntity offer, @PathVariable Integer id) {
		OfferEntity offerDb = findOfferById(id);
		if (offerDb != null) {
			offerDb.setOfferName(offer.getOfferName());
			offerDb.setOfferDescription(offer.getOfferDescription());
			offerDb.setOfferCreated(offer.getOfferCreated());
			offerDb.setOfferExpires(offer.getOfferExpires());
			offerDb.setRegularPrice(offer.getRegularPrice());
			offerDb.setActionPrice(offer.getActionPrice());
			offerDb.setImagePath(offer.getImagePath());
			offerDb.setAvailableOffers(offer.getAvailableOffers());
			offerDb.setBoughtOffers(offer.getBoughtOffers());
		}

		return offerDb;
	}

	@DeleteMapping("/{id}")
	public OfferEntity deleteOffer(@PathVariable Integer id) {
		OfferEntity offerDb = findOfferById(id);
		if (offerDb.equals(null))
			return null;
		offers.remove(offerDb);
		return offerDb;
	}

	@GetMapping("/{id}")
	public OfferEntity findOfferById(Integer id) {
		try {
			return getDB().stream().filter(x -> x.getId().equals(id)).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
	
	@PutMapping("/changeOffer/{id}/status/{status}")
	public OfferEntity updateOfferRole(@PathVariable Integer id, @PathVariable EOfferStatus status) {
		OfferEntity offerDb = findOfferById(id);
		if(offerDb.equals(null))
			return null;
		if(status.equals(null)) {
			throw new IllegalArgumentException("Offer status has not found");
		}
			offerDb.setOfferStatus(status);
			return offerDb;
	}

	@GetMapping("/findByPrice/{lowerPrice}/and/{upperPrice}")
	public List<OfferEntity> findOfferBetweenTwoActionPrices(@PathVariable Double lowerPrice,@PathVariable Double upperPrice) {
		return getDB().stream().filter(x -> x.getActionPrice()>lowerPrice && x.getActionPrice()<upperPrice).collect(Collectors.toList());
	}
	
}
