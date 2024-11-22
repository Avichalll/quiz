package com.quiz.quiz.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class RegistrationRequest {

    @NotNull(message = "FirstName should not be null")
    @NotBlank(message = "FirstName should not be Blank")
    @Size(min = 2, max = 50, message = "FirstName should be between 2 and 50 characters")
    private String firstname;

    @NotNull(message = "LastName should not be null")
    @NotBlank(message = "LastName should not be Blank")
    @Size(min = 2, max = 50, message = "LastName should be between 2 and 50 characters")
    private String lastname;

    @Column(name = "contact_Number", unique = true)
    @NotNull(message = "ContactNumber should not be null")
    @NotBlank(message = "ContactNumber should not be Blank")
    @Pattern(regexp = "\\d{10}", message = "Contact number should be a 10-digit number")
    private String contactNumber;

    @NotEmpty(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 charcters long minimum")
    private String password;

}
