package com.iktpreobuka.project.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.project.security.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Public.class)
	private Integer id;

	@Column(name = "first_name")
	@NotBlank(message = "First name must be provided.")
	@JsonView(Views.Private.class)
	private String firstName;

	@Column(name = "last_name")
	@NotBlank(message = "Last name must be provided.")
	@JsonView(Views.Private.class)
	private String lastName;

	@NotBlank(message = "Username must be provided.")
	@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.")
	@JsonView(Views.Public.class)
	private String username;

	@NotBlank(message = "Password must be provided.")
	@Size(min = 5, message = "Password must be between {min} and {max} characters long.")
	@Pattern(regexp = "([0-9].*[a-zA-Z])|([a-zA-Z].*[0-9])", message = "Password must contain only letters and digits.")
	@JsonIgnore
	private String password;

	@NotBlank(message = "Email must be provided.")
	@JsonView(Views.Private.class)
	private String email;

	@JsonView(Views.Admin.class)
	private EUserRole role;

	@Version
	@JsonView(Views.Admin.class)
	private Integer version;

	@OneToMany(mappedBy = "seller", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<OfferEntity> offers;

	@OneToMany(mappedBy = "buyer", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<BillEntity> bills;

	@OneToMany(mappedBy = "buyer", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<BillEntity> vouchers;

}
