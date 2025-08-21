package com.SuperToni.SuperToni.auth.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    // User
	@NotBlank
	private String username;

	@NotBlank
	private String authority;

	@NotBlank
	private String password;

    // Both
	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

    //Client
    @NotEmpty
	private String city;

    @NotEmpty
    private String address;

    @NotNull
    private Integer phoneNumber;

    @Email
    @NotEmpty
    private String email;


    
}
