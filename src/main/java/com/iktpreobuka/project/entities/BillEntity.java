package com.iktpreobuka.project.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.project.security.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillEntity {

	public BillEntity(@NotNull Boolean paymentMade, @NotNull Boolean paymentCanceled, @NotNull LocalDate billCreated,
			OfferEntity offer, UserEntity buyer) {
		super();
		this.paymentMade = paymentMade;
		this.paymentCanceled = paymentCanceled;
		this.billCreated = billCreated;
		this.offer = offer;
		this.buyer = buyer;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Public.class)
	private Integer id;
	
	@Column(name = "payment_made")
	@NotNull
	@JsonView(Views.Admin.class)
	private Boolean paymentMade;
	
	@Column(name = "payment_canceled")
	@NotNull
	@JsonView(Views.Admin.class)
	private Boolean paymentCanceled;
	
	@Column(name = "bill_created")
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonView(Views.Public.class)
	private LocalDate billCreated;
	
	@Version
	@JsonView(Views.Admin.class)
	private Integer version;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "offer_id")
	@JsonView(Views.Private.class)
	private OfferEntity offer;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "buyer_id")
	@JsonView(Views.Private.class)
	private UserEntity buyer;

}
