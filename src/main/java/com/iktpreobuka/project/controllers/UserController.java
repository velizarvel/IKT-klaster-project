package com.iktpreobuka.project.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.project.entities.EUserRole;
import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.repositories.UserRepository;
import com.iktpreobuka.project.security.Views;
import com.iktpreobuka.project.utils.RESTError;
import com.iktpreobuka.project.utils.Validation;

@RestController
@RequestMapping(value = "/project/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public List<UserEntity> findAllUsers() {
		return (List<UserEntity>) userRepository.findAll();
	}

	@GetMapping("/public")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> findAllUsersPublic() {
		try {
			return new ResponseEntity<>((List<UserEntity>) userRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/private")
	@JsonView(Views.Private.class)
	public ResponseEntity<?> findAllUsersPrivate(BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(new RESTError(HttpStatus.BAD_REQUEST.value(), result.toString()),
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>((List<UserEntity>) userRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/admin")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> findAllUsersAdmin(BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(new RESTError(HttpStatus.BAD_REQUEST.value(), result.toString()),
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>((List<UserEntity>) userRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findUserById(@PathVariable Integer id) {
		try {
			UserEntity user = userRepository.findById(id).orElse(null);
			if (user == null) {
				return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "User not found"),
						HttpStatus.OK);
			}

			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/")
	public ResponseEntity<?> addUser(@Valid @RequestBody UserEntity user, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(Validation.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserEntity user, @PathVariable Integer id,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Validation.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		UserEntity userDb = Validation.validateEntity(id, userRepository);

		userDb.setFirstName(Validation.setIfNotNull(userDb.getFirstName(), user.getFirstName()));
		userDb.setLastName(Validation.setIfNotNull(userDb.getLastName(), user.getLastName()));
		userDb.setEmail(Validation.setIfNotNull(userDb.getEmail(), user.getEmail()));
		userDb.setUsername(Validation.setIfNotNull(userDb.getUsername(), user.getUsername()));

		return new ResponseEntity<>(userRepository.save(userDb), HttpStatus.OK);

	}

	@PutMapping("/change/{id}/role{role}")
	public ResponseEntity<?> updateUserRole(@PathVariable Integer id, @PathVariable EUserRole role) {
		Optional<UserEntity> userDb = userRepository.findById(id);
		if (userDb.isPresent()) {
			userDb.get().setRole(role);
			userRepository.save(userDb.get());
			return new ResponseEntity<>(userDb, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "User not found"),
				HttpStatus.NOT_FOUND);
	}

	@PutMapping("/changePassword/{id}")
	public ResponseEntity<?> updateUserPassword(@PathVariable Integer id, @RequestParam String oldPass,
			@RequestParam String newPass) {
		Optional<UserEntity> userDb = userRepository.findById(id);
		if (userDb.isPresent()) {
			if (!userDb.get().getPassword().equals(oldPass)) {
				return new ResponseEntity<>(new RESTError(HttpStatus.UNAUTHORIZED.value(), "Old password is incorrect"),
						HttpStatus.UNAUTHORIZED);
			}
			userDb.get().setPassword(newPass);
			userRepository.save(userDb.get());

			return new ResponseEntity<>(userDb, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "User not found"),
				HttpStatus.NOT_FOUND);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
		Optional<UserEntity> userDb = userRepository.findById(id);
		if (userDb.isPresent()) {
			userRepository.delete(userDb.get());
			return new ResponseEntity<>(userDb, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "User not found"),
				HttpStatus.NOT_FOUND);
	}

	@GetMapping("/by-username/{username}")
	public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
		Optional<UserEntity> user = userRepository.findByUsername(username);

		if (user.isPresent()) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		}

		return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "User not found"),
				HttpStatus.NOT_FOUND);
	}

}
