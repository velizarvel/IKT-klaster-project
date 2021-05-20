package com.iktpreobuka.project.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class CategoryEntity {

	private Integer id;
	private String categoryName;
	private String categoryDescription;
	
}
