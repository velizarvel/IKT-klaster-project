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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.project.security.Views;

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
	@JsonView(Views.Public.class)
	private Integer id;
	
	@Column(name = "offer_name")
	@NotBlank(message = "Name must be provided.")
	@JsonView(Views.Public.class)
	private String offerName;
	
	@Column(name = "offer_description")
	@NotBlank(message = "Description must be provided.")
	@Size(min = 5, max = 20, message = "Description name must be between {min} and {max} characters long.")
	@JsonView(Views.Public.class)
	private String offerDescription;
	
	@Column(name = "offer_created")
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonView(Views.Public.class)
	private LocalDate offerCreated;
	
	@Column(name = "offer_expires")
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonView(Views.Public.class)
	private LocalDate offerExpires;
	@Column(name = "regular_price")
	@Min(value = 1, message = "Regular price must be greater than 1.")
	@JsonView(Views.Public.class)
	private Double regularPrice;
	
	@Column(name = "action_price")
	@Min(value = 1, message = "Action price must be greater than 1.")
	@JsonView(Views.Public.class)
	private Double actionPrice;
	
	@Column(name = "image_path")
	@NotBlank(message = "Image path must be provided.")
	@JsonView(Views.Public.class)
	private String imagePath;
	
	@Column(name = "available_offers")
	@Min(value = 1, message = "Available offers must be greater than 0.")
	private Integer availableOffers;
	
	@Column(name = "bought_offers")
	@Min(value = 0, message = "Bought offers must be equal to or greater than 0.")
	@JsonView(Views.Public.class)
	private Integer boughtOffers;
	
	@Column(name = "offerStatus")
	@NotNull
	@JsonView(Views.Public.class)
	private EOfferStatus offerStatus;
	
	@Version
	@JsonView(Views.Public.class)
	private Integer version;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@JsonView(Views.Public.class)
	private CategoryEntity category;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id")
	@JsonView(Views.Public.class)
	private UserEntity seller;
	
	@OneToMany(mappedBy = "offer", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<BillEntity> bills;
	
	@OneToMany(mappedBy = "offer", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<VoucherEntity> vouchers;
}
