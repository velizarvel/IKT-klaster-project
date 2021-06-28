package com.iktpreobuka.project.utils;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.project.security.Views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RESTError {

	@JsonView(Views.Public.class)
	private int code;
	@JsonView(Views.Public.class)
	private String message;

}