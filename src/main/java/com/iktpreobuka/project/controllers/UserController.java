package com.iktpreobuka.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.EUserRole;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.helpers.Validation;
import com.iktpreobuka.project.repositories.UserRepository;

@RestController
@RequestMapping(value = "/project/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public List<UserEntity> findAllUsers() {
		return (List<UserEntity>) userRepository.findAll();
	}

	@GetMapping("/{id}")
	public Optional<UserEntity> findUserById(@PathVariable Integer id) {
		return userRepository.findById(id);
	}

	@PostMapping("/")
	public UserEntity addUser(@RequestBody UserEntity user) {
		return userRepository.save(user);
	}

	@PutMapping("/{id}")
	public UserEntity updateUser(@RequestBody UserEntity user, @PathVariable Integer id) {

		UserEntity userDb = Validation.validateEntity(id, userRepository);

		userDb.setFirstName(Validation.setIfNotNull(userDb.getFirstName(), user.getFirstName()));
		userDb.setLastName(Validation.setIfNotNull(userDb.getLastName(), user.getLastName()));
		userDb.setEmail(Validation.setIfNotNull(userDb.getEmail(), user.getEmail()));
		userDb.setUsername(Validation.setIfNotNull(userDb.getUsername(), user.getUsername()));

		return userRepository.save(userDb);

	}

	@PutMapping("/change/{id}/role{role}")
	public Optional<UserEntity> updateUserRole(@PathVariable Integer id, @PathVariable EUserRole role) {
		Optional<UserEntity> userDb = userRepository.findById(id);
		if (userDb.isPresent()) {
			userDb.get().setRole(role);
			userRepository.save(userDb.get());
		}

		return userDb;
	}

	@PutMapping("/changePassword/{id}")
	public Optional<UserEntity> updateUserPassword(@PathVariable Integer id, @RequestParam String oldPass,
			@RequestParam String newPass) {
		Optional<UserEntity> userDb = userRepository.findById(id);
		if (userDb.isPresent()) {
			if (!userDb.get().getPassword().equals(oldPass)) {
				throw new IllegalArgumentException("Old password is incorrect");
			}
			userDb.get().setPassword(newPass);
			userRepository.save(userDb.get());
		}
		return userDb;

	}

	@DeleteMapping("/{id}")
	public Optional<UserEntity> deleteUser(@PathVariable Integer id) {
		Optional<UserEntity> userDb = userRepository.findById(id);
		if (userDb.isPresent()) {
			userRepository.delete(userDb.get());
		}

		return userDb;
	}

	@GetMapping("/by-username/{username}")
	public Optional<UserEntity> findUserByUsername(@PathVariable String username) {
		return userRepository.findByUsername(username);
	}

}
