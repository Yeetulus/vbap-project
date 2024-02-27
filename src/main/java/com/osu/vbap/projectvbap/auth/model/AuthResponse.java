package com.osu.vbap.projectvbap.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.osu.vbap.projectvbap.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("role")
    private Role userRole;
    @JsonProperty("user_id")
    private Long userId;
}