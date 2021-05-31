package com.iktpreobuka.project.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.project.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer>{

	public Optional<UserEntity> findByUsername(String username);
	
}
