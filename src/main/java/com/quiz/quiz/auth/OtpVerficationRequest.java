package com.quiz.quiz.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerficationRequest {

    @Size(min = 6, max = 6, message = "OTP must contain exactly 6 digits")
    @Pattern(regexp = "\\d{6}", message = "OTP must be a 6-digit number")
    private String otp;

    @NotEmpty(message = "Phone Number should not Empty")
    @NotBlank(message = "Phone Number should not Blank")
    @Pattern(regexp = "\\d{10}", message = "Contact number should be a 10-digit number")
    private String contactNumber;

}
