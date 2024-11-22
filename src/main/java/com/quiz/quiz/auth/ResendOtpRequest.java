package com.quiz.quiz.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResendOtpRequest {

    @NotNull(message = "ContactNumber should not be null")
    @NotEmpty(message = "ContactNumber should not be empty")
    @Pattern(regexp = "\\d{10}", message = "Contact number should be a 10-digit number")
    private String contactNumber;

}
