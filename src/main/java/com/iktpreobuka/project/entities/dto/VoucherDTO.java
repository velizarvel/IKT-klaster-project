package com.iktpreobuka.project.entities.dto;

import java.time.LocalDate;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTO {

	@NotBlank(message = "Expiration date must be provided.")
	@Future(message = "Expiration date must be date in the future.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	protected LocalDate expirationDate;

	@AssertFalse
	protected Boolean isUsed;

	@NotBlank(message = "Offer must be provided.")
	private OfferEntity offer;

	@NotBlank(message = "Buyer must be provided.")
	private UserEntity buyer;

}
