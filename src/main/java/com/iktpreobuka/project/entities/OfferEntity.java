package com.iktpreobuka.project.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "offer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class OfferEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name = "offer_name")
	@NotNull
	private String offerName;
	@Column(name = "offer_description")
	private String offerDescription;
	@Column(name = "offer_created")
	@NotNull
	private LocalDate offerCreated;
	@Column(name = "offer_expires")
	@NotNull
	private LocalDate offerExpires;
	@Column(name = "regular_price")
	@NotNull
	private Double regularPrice;
	@Column(name = "action_price")
	private Double actionPrice;
	@Column(name = "image_path")
	private String imagePath;
	@Column(name = "available_offers")
	private Integer availableOffers;
	@Column(name = "bought_offers")
	private Integer boughtOffers;
	@Column(name = "offerStatus")
	@NotNull
	private EOfferStatus offerStatus;
	@Version
	private Integer version;
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private CategoryEntity category;
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id")
	private UserEntity seller;
	@OneToMany(mappedBy = "offer", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<BillEntity> bills;
	@OneToMany(mappedBy = "offer", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<VoucherEntity> vouchers;
}
