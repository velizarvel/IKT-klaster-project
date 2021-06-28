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
@Table(name = "voucher")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherEntity {

	public VoucherEntity(@NotNull LocalDate expirationDate, @NotNull Boolean isUsed, OfferEntity offer,
			UserEntity buyer) {
		super();
		this.expirationDate = expirationDate;
		this.isUsed = isUsed;
		this.offer = offer;
		this.buyer = buyer;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Public.class)
	private Integer id;

	@Column(name = "expiration_date")
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonView(Views.Public.class)
	private LocalDate expirationDate;

	@Column(name = "isUsed")
	@NotNull
	@JsonView(Views.Admin.class)
	private Boolean isUsed;

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
