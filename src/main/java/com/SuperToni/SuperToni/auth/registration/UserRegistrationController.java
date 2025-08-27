package com.SuperToni.SuperToni.auth.registration;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import io.jsonwebtoken.JwtException;
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
  private static final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);



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

	@PostMapping("/token/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
    try {
        String refreshToken = request.get("refresh");
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Refresh token is required"));
        }
        
        // Validate the refresh token
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid refresh token"));
        }
        
        // Extract username from refresh token using your existing method
        String username = jwtUtils.extractUsername(refreshToken);
        
        // Generate new access token using your existing method
        String newAccessToken = jwtUtils.generateToken(username);
        String newRefreshToken = jwtUtils.generateToken(username);
        
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", newAccessToken);
        tokens.put("refresh", newRefreshToken);
        
        return ResponseEntity.ok(tokens);
        
    } catch (JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid refresh token"));
    } catch (Exception e) {
        logger.error("Token refresh failed", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Token refresh failed"));
    }
}

}
