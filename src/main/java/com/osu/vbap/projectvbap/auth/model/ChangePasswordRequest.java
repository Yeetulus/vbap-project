package com.osu.vbap.projectvbap.auth.model;

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
public class ChangePasswordRequest {

    @NotBlank
    private String email;

    private String currentPassword;

    @NotBlank
    @Size(min = 6, message = "New password must be at least 6 characters long.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.,]).{6,}$", message = "Password should contain at least one digit, one lowercase letter, one uppercase letter, and one special character.")
    private String newPassword;
}