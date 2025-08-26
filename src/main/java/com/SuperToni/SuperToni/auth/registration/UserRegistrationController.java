package com.SuperToni.SuperToni.auth.registration;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SuperToni.SuperToni.auth.payload.LoginRequest;
import com.SuperToni.SuperToni.auth.payload.SignUpRequest;
import com.SuperToni.SuperToni.auth.payload.response.JwtResponse;
import com.SuperToni.SuperToni.auth.payload.response.MessageResponse;
import com.SuperToni.SuperToni.configuration.jwt.JwtService;
import com.SuperToni.SuperToni.configuration.services.UserDetailsImpl;
import com.SuperToni.SuperToni.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "The Authentication API based on JWT")
public class UserRegistrationController {
  private final AuthenticationManager authenticationManager;
  private final UserRegistrationService userRegistrationService;
  private final JwtService jwtUtils;
  private final UserService userService;


  @Autowired
	public UserRegistrationController(AuthenticationManager authenticationManager, UserService userService, JwtService jwtUtils,
			UserRegistrationService userRegistrationService) {
		this.userService = userService;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.userRegistrationService = userRegistrationService;
	}

  @PostMapping("/signup")
  public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

    if (userService.existsUser(signUpRequest.getUsername()).equals(true)) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }
    userRegistrationService.createUser(signUpRequest);
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	
  }

  @PostMapping("/signin")
	public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	
		try{
			
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateToken(loginRequest.getUserName());

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

			return ResponseEntity.ok().body(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
		}catch(BadCredentialsException exception){
			return ResponseEntity.badRequest().body("Bad Credentials!");
		}
	}

	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
		Boolean isValid = jwtUtils.validateJwtToken(token);
		return ResponseEntity.ok(isValid);
	}

}
