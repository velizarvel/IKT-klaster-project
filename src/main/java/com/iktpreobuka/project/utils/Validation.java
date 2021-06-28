package com.iktpreobuka.project.utils;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class Validation {

	public static <T> T setIfNotNull(T oldProperty, T newProperty) {
		return newProperty.equals(null) ? oldProperty : newProperty;
	}

	public static <T, S> T validateEntity(S entityId, CrudRepository<T, S> repository) {
		Optional<T> optional = repository.findById(entityId);
		if (!optional.isPresent()) {
			throw new InvalidParameterException("The entity with id " + entityId + " does not exist.");
		}

		return optional.get();
	}

	public static Object createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" \n"));

	}

}
