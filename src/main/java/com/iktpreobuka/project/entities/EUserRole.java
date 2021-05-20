package com.iktpreobuka.project.entities;

public enum EUserRole {
	
	ROLE_CUSTOMER, ROLE_ADMIN, ROLE_SELLER;
	
	public static EUserRole getRoleByInt(int role) {
		switch (role) {
		case 0:
			 return EUserRole.ROLE_CUSTOMER;
		case 1:
			return EUserRole.ROLE_ADMIN;
		case 2: 
			return EUserRole.ROLE_SELLER;
		default:
			return null;
		}
	}

}
