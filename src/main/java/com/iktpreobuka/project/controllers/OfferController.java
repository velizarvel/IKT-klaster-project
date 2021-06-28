package com.iktpreobuka.project.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.management.relation.InvalidRoleValueException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iktpreobuka.project.entities.CategoryEntity;
import com.iktpreobuka.project.entities.EOfferStatus;
import com.iktpreobuka.project.entities.EUserRole;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.repositories.CategoryRepository;
import com.iktpreobuka.project.repositories.OfferRepository;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.services.OfferService;
import com.iktpreobuka.project.utils.RESTError;
import com.iktpreobuka.project.utils.Validation;

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
	public ResponseEntity<?> findAllOffers() {

		try {
			return new ResponseEntity<>((List<OfferEntity>) offerRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{categoryId}/seller/{sellerId}")
	public ResponseEntity<?> addOffer(@PathVariable Integer categoryId, @PathVariable Integer sellerId,
			@Valid @RequestBody OfferEntity offer, BindingResult result) throws InvalidRoleValueException {

		if (result.hasErrors()) {
			return new ResponseEntity<>(new RESTError(HttpStatus.BAD_REQUEST.value(), result.toString()),
					HttpStatus.BAD_REQUEST);
		}

		UserEntity seller = Validation.validateEntity(sellerId, userRepository);

		if (!seller.getRole().equals(EUserRole.ROLE_SELLER)) {
			return new ResponseEntity<>(new RESTError(HttpStatus.UNAUTHORIZED.value(),
					"The seller doesn't have property role for adding offer."), HttpStatus.UNAUTHORIZED);
		}

		CategoryEntity category = Validation.validateEntity(categoryId, categoryRepository);

		offer.setSeller(seller);
		offer.setCategory(category);

		LocalDate offerCreated = LocalDate.now();
		LocalDate offerExpires = offerCreated.plusDays(10);

		offer.setOfferCreated(offerCreated);
		offer.setOfferExpires(offerExpires);

		offer.setOfferStatus(EOfferStatus.WAIT_FOR_APPROVING);

		return new ResponseEntity<>(offerRepository.save(offer), HttpStatus.CREATED);
	}

	@PutMapping("/{id}/category/{categoryId}")
	public ResponseEntity<?> updateOffer(@Valid @RequestBody OfferEntity offer, @PathVariable Integer id,
			@PathVariable Integer categoryId, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(new RESTError(HttpStatus.BAD_REQUEST.value(), result.toString()),
					HttpStatus.BAD_REQUEST);
		}

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

			return new ResponseEntity<>(offerRepository.save(updatedOffer), HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Offer not found"), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOffer(@PathVariable Integer id) {
		Optional<OfferEntity> offerDb = offerRepository.findById(id);
		if (offerDb.isPresent()) {
			offerRepository.delete(offerDb.get());
			return new ResponseEntity<>(offerDb, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "User not found"),
				HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findOfferById(Integer id) {
		Optional<OfferEntity> offer = offerRepository.findById(id);
		if (offer.isPresent()) {
			return new ResponseEntity<>(offer, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Offer not found"),
				HttpStatus.NOT_FOUND);
	}

	@PutMapping("/changeOffer/{id}/status/{status}")
	public ResponseEntity<?> updateOfferStatus(@PathVariable Integer id, @PathVariable EOfferStatus status) {

		Optional<OfferEntity> offer = offerService.changeOfferStatus(id, status);
		if (offer.isPresent()) {
			return new ResponseEntity<>(offer, HttpStatus.OK);
		}
		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Offer not found"),
				HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByPrice/{lowerPrice}/and/{upperPrice}")
	public ResponseEntity<?> findOffersBetweenTwoActionPrices(@PathVariable Double lowerPrice,
			@PathVariable Double upperPrice) {
		try {
			List<OfferEntity> offers = offerRepository.findAllByActionPriceBetween(lowerPrice, upperPrice);
			return new ResponseEntity<>(offers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/project/offers/uploadImage/{id}")
	public ResponseEntity<?> uploadPicture(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {

		OfferEntity offer = offerRepository.findById(id).orElse(null);
		if (offer != null) {
			offer.setImagePath(file.getOriginalFilename());
			offerRepository.save(offer);
			return new ResponseEntity<>(offer, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Offer not found"),
				HttpStatus.NOT_FOUND);

	}

}
