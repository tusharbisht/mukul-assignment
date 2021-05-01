package com.apigateway.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/customer")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public String customerOrAdminAccess() {
		return "CUSTOMER";
	}

	@GetMapping("/merchant")
	@PreAuthorize("hasRole('MERCHANT')")
	public String merchantAccess() {
		return "Merchant";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin";
	}
}