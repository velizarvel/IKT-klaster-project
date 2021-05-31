package com.iktpreobuka.project.helpers;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

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

}
