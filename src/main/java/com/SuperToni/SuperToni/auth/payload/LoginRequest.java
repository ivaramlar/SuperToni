package com.SuperToni.SuperToni.auth.payload;

import io.micrometer.common.lang.NonNull;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    //User
    @Size(min=3, max=20)
    @NonNull
    private String userName;

    @NonNull
    private String password;

    
}
