package com.peace.reg.rest;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peace.reg.CustomErrorType;
import com.peace.reg.dto.UserDTO;
import com.peace.reg.repository.UserJpaRepository;

@RestController
@RequestMapping("/api/user")
public class UserRegistrationRestController {
	
	public static final Logger logger = 
			LoggerFactory.getLogger(UserRegistrationRestController.class);
	
	private UserJpaRepository userJpaRepository;

	@Autowired
	public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> listAllUsers(){
		List<UserDTO> users = userJpaRepository.findAll();
		if(users.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<UserDTO>>(users,HttpStatus.OK);
	}
	
	@PostMapping(value="/",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody final UserDTO user){
		
		logger.info("Creating User : {}",user);
		if(userJpaRepository.findByName(user.getName()) !=null) {
			logger.error("Unable to create. A User with name {} already exist",
					user.getName());
			return new ResponseEntity<>(new CustomErrorType(
					"Unable to create new user. A User with name "
							+ user.getName() + " already exist."),HttpStatus.CONFLICT);
		}
		userJpaRepository.save(user);
		return new ResponseEntity<UserDTO>(user,HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable("id") final Long id){
		
		UserDTO user;
		try {
			user = userJpaRepository.findById(id).get();
		} catch (Exception e) {
			e.printStackTrace();
			
			return new ResponseEntity<>(new CustomErrorType("User with id "
					+ id + " not found"),HttpStatus.NOT_FOUND);
		}
			
		return new ResponseEntity<UserDTO>(user,HttpStatus.OK);
		
	}
	
	@PutMapping(value="/{id}",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> updateUser(@PathVariable("id") final Long id,@RequestBody UserDTO user){
		
		UserDTO currentUser = null;
		try {
			currentUser = userJpaRepository.findById(id).get();
			currentUser.setAddress(user.getAddress());
			currentUser.setEmail(user.getEmail());
			currentUser.setName(user.getName());
			
			userJpaRepository.saveAndFlush(currentUser);
			
			return new ResponseEntity<UserDTO>(currentUser,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<UserDTO>(new CustomErrorType("Unable to upate. User with id "
					+ id + " not found."),HttpStatus.NOT_FOUND);
		}
		
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") final Long id){
		try {
			userJpaRepository.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<UserDTO>(new CustomErrorType("Unable to delete. User with id "
					+ id + " not found."),HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<UserDTO>(HttpStatus.NO_CONTENT);
	}
	

}
