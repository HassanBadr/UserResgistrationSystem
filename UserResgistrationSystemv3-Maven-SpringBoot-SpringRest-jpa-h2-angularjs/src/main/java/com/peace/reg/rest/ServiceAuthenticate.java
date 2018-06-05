package com.peace.reg.rest;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceAuthenticate {
	
	@RequestMapping("/userauth")
	public Principal user(Principal user) {
		return user;
	}
	

}
