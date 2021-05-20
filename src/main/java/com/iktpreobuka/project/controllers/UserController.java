package com.iktpreobuka.project.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

@RestController
@RequestMapping(value = "/project/users")
public class UserController {

	private List<UserEntity> users = new ArrayList<UserEntity>();

	private List<UserEntity> getDB() {
		if (users.size() == 0) {
			UserEntity u1 = new UserEntity(1, "Vladimir", "Dimitrieski", "dimitrieski@uns.ac.rs", "vladimir",
					"vladimir", EUserRole.ROLE_CUSTOMER);
			UserEntity u2 = new UserEntity(2, "Milan", "Celikovic", "milancel@uns.ac.rs", "milan", "milan",
					EUserRole.ROLE_CUSTOMER);
			UserEntity u3 = new UserEntity(3, "Nebojsa", "Horvat", "horva.n@uns.ac.rs", "nebojsa", "nebojsa",
					EUserRole.ROLE_CUSTOMER);
			users.add(u1);
			users.add(u2);
			users.add(u3);
		}
		return users;
	}

	@GetMapping("/")
	public List<UserEntity> findAllUsers() {
		return getDB();
	}
	
	@GetMapping("/{id}")
	public Optional<UserEntity> findUserById(@PathVariable Integer id) {
		return getDB().stream().filter(x -> x.getId().equals(id)).findFirst();
	}
	
	
	@PostMapping("/")
	public String addUser(@RequestBody UserEntity user) {
		Integer id = Math.abs(new Random().nextInt());
		UserEntity newUser = new UserEntity(id, user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getEmail(), EUserRole.ROLE_CUSTOMER);
		if(!newUser.equals(null)) {
			users.add(newUser);
			return "Successfully added new user" + newUser.toString();
		}
		return "Something is wrong";
	}
	
	@PutMapping("/{id}")
	public UserEntity updateUser(@RequestBody UserEntity user, @PathVariable Integer id) {
		UserEntity userDb = findUserById(id).get();
		if (userDb!=null) {
			userDb.setFirstName(user.getFirstName());
			userDb.setLastName(user.getLastName());
			userDb.setEmail(user.getEmail());
			userDb.setUsername(user.getUsername());
		}
		
		return userDb;
	}
	
	@PutMapping("/change/{id}/role{role}")
	public UserEntity updateUserRole(@PathVariable Integer id, @PathVariable Integer role) {
		UserEntity userDb = findUserById(id).get();
		if(userDb.equals(null))
			return null;
		EUserRole newRole = EUserRole.getRoleByInt(role);
		if(role.equals(null)) {
			throw new IllegalArgumentException("Role has not foud");
		}
			userDb.setRole(newRole);
			return userDb;
	}
	
	
	@PutMapping("/changePassword/{id}")
	public UserEntity updateUserPassword(@PathVariable Integer id, @RequestParam String oldPass, @RequestParam String newPass) {
		UserEntity userDb = findUserById(id).get();
		if(userDb.equals(null))
			return null;
		if(!userDb.getPassword().equals(oldPass)) {
			throw new IllegalArgumentException("Old password is incorrect");
		}
			userDb.setPassword(newPass);
			return userDb;

	}
	
	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable Integer id) {
		UserEntity userDb = findUserById(id).get();
		if(userDb.equals(null))
			return null;
		users.remove(userDb);
		return "Successfully delete user " + userDb.toString();
	}
	
	@GetMapping("/by-username/{username}")
	public Optional<UserEntity> findUserByUsername(@PathVariable String username) {
		return getDB().stream().filter(x -> x.getUsername().equals(username)).findFirst();
	}
	
	

}
