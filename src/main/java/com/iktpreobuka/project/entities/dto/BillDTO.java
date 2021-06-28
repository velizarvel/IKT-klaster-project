package com.iktpreobuka.project.entities.dto;

import java.time.LocalDate;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

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
public class BillDTO {

	private Integer id;

	@NotNull(message = "Payment made must not be null or blank.")
	@AssertFalse
	private Boolean paymentMade;

	@NotNull(message = "Payment canceled must not be null or blank.")
	@AssertFalse
	private Boolean paymentCanceled;

	@PastOrPresent(message = "Bill created can not be in the future.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate billCreated;

	private UserEntity buyer;

	private OfferEntity offer;

}
