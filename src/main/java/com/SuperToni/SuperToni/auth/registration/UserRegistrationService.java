package com.SuperToni.SuperToni.auth.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.SuperToni.SuperToni.auth.payload.SignUpRequest;
import com.SuperToni.SuperToni.client.Client;
import com.SuperToni.SuperToni.client.ClientService;
import com.SuperToni.SuperToni.user.Authorities;
import com.SuperToni.SuperToni.user.AuthoritiesService;
import com.SuperToni.SuperToni.user.User;
import com.SuperToni.SuperToni.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Service
public class UserRegistrationService {

	private final PasswordEncoder encoder;
	private final AuthoritiesService authoritiesService;
	private final UserService userService;
	private final ClientService clientService;

	@Autowired
	public UserRegistrationService(PasswordEncoder encoder, AuthoritiesService authoritiesService, UserService userService,
			ClientService clientService) {
		this.encoder = encoder;
		this.authoritiesService = authoritiesService;
		this.userService = userService;
		this.clientService = clientService;
		
	}

	@Transactional
	public void createUser(@Valid SignUpRequest request) {
		User user = new User();
		user.setUserName(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		String strRoles = request.getAuthority();
		Authorities role;

		switch (strRoles.toLowerCase()) {
			case "admin":
				role = authoritiesService.findByAuthority("ADMIN");
				user.setAuthority(role);
				userService.saveUser(user);
				break;
			default:
				role = authoritiesService.findByAuthority("CLIENT");
				user.setAuthority(role);
				userService.saveUser(user);
				Client client = new Client();
				client.setFirstName(request.getFirstName());
				client.setLastName(request.getLastName());
				client.setAddress(request.getAddress());
				client.setCity(request.getCity());
				client.setPhoneNumber(request.getPhoneNumber());
				client.setEmail(request.getEmail());
				client.setUser(user);
				clientService.saveClient(client);

		}
	}

    
}
