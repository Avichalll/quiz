package com.quiz.quiz.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(

                info = @Info(contact = @Contact(name = "Avichal", email = "avichal@gmail.com"

                ), description = "OpenApi documentation for Nodex", title = "OpenApi specification - Nodex", version = "1.0.0"

                ), servers = {
                                @Server(description = "Local ENV", url = "http://localhost:9090/api/v1")
                }, security = {
                                @SecurityRequirement(name = "bearerAuth")
                })

@SecurityScheme(name = "bearerAuth", description = "JWT auth description", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)

public class OpenApiConfig {

}
