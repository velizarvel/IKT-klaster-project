package com.iktpreobuka.project.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class UserEntity {
	
	private Integer id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private EUserRole role;
	
	
	@Override
	public String toString() {
		return "id =" + id + ", firstName =" + firstName + ", lastName =" + lastName + ", username =" + username
				+ ", password =" + password + ", email =" + email + ", role =" + role + "]";
	}
	
	

}
