package com.SuperToni.SuperToni.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
    info = @Info(
        title = "SuperToni API",
        version = "1.0",
        description = "API documentation for SuperToni application",
        contact = @Contact(
        name = "Iván Ramírez", email = "ivan.ramirez.lara18@gmail.com"
        )
        
    )
)

@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfiguration {
    
}
