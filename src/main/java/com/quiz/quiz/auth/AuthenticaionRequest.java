package com.quiz.quiz.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticaionRequest {

    @NotBlank(message = "PhoneNumber is mandatory")
    @NotEmpty(message = "PhoneNumber is mandatory")
    private String contactNumber;

    @NotEmpty(message = "Password is mandatory")
    @NotBlank(message = "password is mandatory")
    @Size(min = 8, message = "Password should be minimum 8 character long")
    private String password;

}
