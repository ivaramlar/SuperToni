package com.SuperToni.SuperToni.auth.registration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.SuperToni.SuperToni.auth.payload.SignUpRequest;
import com.SuperToni.SuperToni.auth.payload.response.MessageResponse;
import com.SuperToni.SuperToni.configuration.jwt.JwtService;
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

}
