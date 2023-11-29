package com.osu.vbap.projectvbap.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name cannot be empty")
    private String firstname;
    @NotBlank(message = "Last name cannot be empty")
    private String lastname;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid")
    private String email;
    @Size(min = 6, message = "Password should be at least 6 characters long.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.,]).{6,}$", message = "Password should contain at least one digit, one lowercase letter, one uppercase letter, and one special character.")
    private String password;
}