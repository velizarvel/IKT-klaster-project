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
	private Integer id;
	@Column(name = "expiration_date")
	@NotNull
	private LocalDate expirationDate;
	@Column(name = "isUsed")
	@NotNull
	private Boolean isUsed;
	@Version
	private Integer version;
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "offer_id")
	private OfferEntity offer;
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "buyer_id")
	private UserEntity buyer;

}
