package com.iktpreobuka.project.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.management.relation.InvalidRoleValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.entities.EOfferStatus;
import com.iktpreobuka.project.entities.EUserRole;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.helpers.Validation;
import com.iktpreobuka.project.repositories.CategoryRepository;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.services.OfferService;

@RestController
@RequestMapping(path = "/project/offers")
public class OfferController {

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private OfferService offerService;

	@GetMapping("/")
	public List<OfferEntity> findAllOffers() {
		return (List<OfferEntity>) offerRepository.findAll();
	}

	@PostMapping("/{categoryId}/seller/{sellerId}")
	public OfferEntity addOffer(@PathVariable Integer categoryId, @PathVariable Integer sellerId,
			@RequestBody OfferEntity offer) throws InvalidRoleValueException {

		UserEntity seller = Validation.validateEntity(sellerId, userRepository);

		if (!seller.getRole().equals(EUserRole.ROLE_SELLER)) {
			throw new InvalidRoleValueException("The seller doesn't have property role for adding offer.");
		}

		CategoryEntity category = Validation.validateEntity(categoryId, categoryRepository);

		offer.setSeller(seller);
		offer.setCategory(category);

		LocalDate offerCreated = LocalDate.now();
		LocalDate offerExpires = offerCreated.plusDays(10);

		offer.setOfferCreated(offerCreated);
		offer.setOfferExpires(offerExpires);

		offer.setOfferStatus(EOfferStatus.WAIT_FOR_APPROVING);

		return offerRepository.save(offer);
	}

	@PutMapping("/{id}/category/{categoryId}")
	public OfferEntity updateOffer(@RequestBody OfferEntity offer, @PathVariable Integer id,
			@PathVariable Integer categoryId) {

		Optional<OfferEntity> offerDb = offerRepository.findById(id);

		if (offerDb.isPresent()) {

			OfferEntity updatedOffer = offerDb.get();

			CategoryEntity category = Validation.validateEntity(categoryId, categoryRepository);
			updatedOffer.setCategory(category);

			updatedOffer.setActionPrice(Validation.setIfNotNull(updatedOffer.getActionPrice(), offer.getActionPrice()));
			updatedOffer.setAvailableOffers(
					Validation.setIfNotNull(updatedOffer.getAvailableOffers(), offer.getAvailableOffers()));
			updatedOffer
					.setBoughtOffers(Validation.setIfNotNull(updatedOffer.getBoughtOffers(), offer.getBoughtOffers()));
			updatedOffer.setImagePath(Validation.setIfNotNull(updatedOffer.getImagePath(), offer.getImagePath()));
			updatedOffer
					.setOfferCreated(Validation.setIfNotNull(updatedOffer.getOfferCreated(), offer.getOfferCreated()));
			updatedOffer
					.setOfferExpires(Validation.setIfNotNull(updatedOffer.getOfferExpires(), offer.getOfferExpires()));
			updatedOffer.setOfferDescription(
					Validation.setIfNotNull(updatedOffer.getOfferDescription(), offer.getOfferDescription()));
			updatedOffer.setOfferName(Validation.setIfNotNull(updatedOffer.getOfferName(), offer.getOfferName()));
			updatedOffer
					.setRegularPrice(Validation.setIfNotNull(updatedOffer.getRegularPrice(), offer.getRegularPrice()));
			updatedOffer.setSeller(Validation.setIfNotNull(updatedOffer.getSeller(), offer.getSeller()));
			updatedOffer.setVersion(Validation.setIfNotNull(updatedOffer.getVersion(), offer.getVersion()));

			return offerRepository.save(updatedOffer);
		}

		return null;
	}

	@DeleteMapping("/{id}")
	public Optional<OfferEntity> deleteOffer(@PathVariable Integer id) {
		Optional<OfferEntity> offerDb = offerRepository.findById(id);
		if (offerDb.isPresent()) {
			offerRepository.delete(offerDb.get());
		}

		return offerDb;
	}

	@GetMapping("/{id}")
	public Optional<OfferEntity> findOfferById(Integer id) {
		return offerRepository.findById(id);
	}

	@PutMapping("/changeOffer/{id}/status/{status}")
	public Optional<OfferEntity> updateOfferStatus(@PathVariable Integer id, @PathVariable EOfferStatus status) {
		return offerService.changeOfferStatus(id, status);
	}

	@GetMapping("/findByPrice/{lowerPrice}/and/{upperPrice}")
	public List<OfferEntity> findOffersBetweenTwoActionPrices(@PathVariable Double lowerPrice,
			@PathVariable Double upperPrice) {
		return offerRepository.findAllByActionPriceBetween(lowerPrice, upperPrice);
	}

	@PostMapping("/project/offers/uploadImage/{id}")
	public OfferEntity uploadPicture(@PathVariable Integer id, String imagePath) {

		OfferEntity offer = offerRepository.findById(id).orElse(null);
		if (offer != null) {
			offer.setImagePath(imagePath);
			offerRepository.save(offer);
		}

		return offer;

	}

}
