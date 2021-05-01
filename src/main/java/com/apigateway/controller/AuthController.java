package com.apigateway.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apigateway.beans.Customer;
import com.apigateway.beans.Driver;
import com.apigateway.beans.ERole;
import com.apigateway.beans.Ride;
import com.apigateway.beans.Role;
import com.apigateway.beans.User;
import com.apigateway.payload.request.LoginRequest;
import com.apigateway.payload.request.SignupRequest;
import com.apigateway.payload.response.JwtResponse;
import com.apigateway.payload.response.MessageResponse;
import com.apigateway.repository.CustomerRepository;
import com.apigateway.repository.DriverRepository;
import com.apigateway.repository.RideRepository;
import com.apigateway.repository.RoleRepository;
import com.apigateway.repository.UserRepository;
import com.apigateway.security.jwt.JwtUtils;
import com.apigateway.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RideRepository rideRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtUtils jwtUtils;
	
	@GetMapping("/hello") String test() {
		Role role = new Role();
		role.setName(ERole.ROLE_CUSTOMER.toString());
		Role role2 = new Role();

		role2.setName(ERole.ROLE_DRIVER.toString());

		roleRepository.save(role);
		roleRepository.save(role2);

		return "hello";
		
	}
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			
			if(!userDetails.isVerified())
			{
				return ResponseEntity
						.status(HttpStatus.FORBIDDEN)
						.body(new MessageResponse<String>("Please verify your email id!"));
			}
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(),
					userDetails.isVerified(), userDetails.isActive(), roles));

		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new MessageResponse<String>("Please enter valid credentials"));
		}
	}

	
//	for registring a new driver with name phone email password and role as driver, FIRSTLY localhost:8765/api/auth/hello to enter Roles in DB
//	Sample body
	/*
		 	{
			    "email"  :" okemail@gmail.com",
			    "password":"Winter@123",
			    "name":"Mukesh",
			    "phone":"9414532752",
			    "roles": ["driver"]
			}
	 * 
	 * Can add both customers and driver
	 * */
	@PostMapping("/register/drivers/new") 
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if(signUpRequest.getName().isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse<String>("Error: No Name is found!"));
		}
		if(signUpRequest.getPhone().isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse<String>("Error: No PhoneNumber is found!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse<String>("Error: Email already regsitered!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getEmail(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.getToken());

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER.toString())
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(customerRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "customer":
					Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER.toString())
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(customerRole);
					break;
				case "driver":
					Role driverRole = roleRepository.findByName(ERole.ROLE_DRIVER.toString())
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(driverRole);
					break;
				default:
					Role customerRole1 = roleRepository.findByName(ERole.ROLE_CUSTOMER.toString())
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(customerRole1);
				}
			});
		}

		user.setRoles(roles);
		if (strRoles.contains("customer")) {
			Customer customer = new Customer();
			customer.setName(user.getEmail());
			customerRepository.save(customer);
		}
		if (strRoles.contains("driver")) {
			Driver driver = new Driver();
			driver.setName(signUpRequest.getName());
			driver.setPhone(signUpRequest.getPhone());
			driverRepository.save(driver);
		}
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse<String>("User registered successfully!"));
	}

	
//	@PostMapping("/rides")
//	public ResponseEntity<?> verifyUser(@RequestParam("token") String token) {
//		if (userRepository.findByToken(token) == null) {
//			return ResponseEntity
//					.badRequest()
//					.body(new MessageResponse<String>("User not found! Please register again"));
//		}
//		
//		List<Ride> ride = rideRepository.findAllById(userRepository.findByToken(token).getId());
//		
//		return ResponseEntity.ok(new MessageResponse<String>("Email Verification Successfull!"));
//		
//	}
	
	@PostMapping("/ride/{ride_id}/accept-ride")
	public ResponseEntity<?> acceptRide(@RequestParam("ride_id") Long ride_id) {
		if (driverRepository.find(token) == null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse<String>("User not found! Please register again"));
		}
		
		Optional<Ride> rides = rideRepository.findById(ride_id);
		
		return ResponseEntity.ok(new MessageResponse<String>(rides.toString()));
		
	}
	
	
	

}
