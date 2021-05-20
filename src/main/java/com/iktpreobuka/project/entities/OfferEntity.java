package com.iktpreobuka.project.entities;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class OfferEntity {
	
	private Integer id;
	private String offerName;
	private String offerDescription;
	private Date offerCreated;
	private Date offerExpires;
	private Double regularPrice;
	private Double actionPrice;
	private String imagePath;
	private Integer availableOffers;
	private Integer boughtOffers;
	private EOfferStatus offerStatus;
		
}
