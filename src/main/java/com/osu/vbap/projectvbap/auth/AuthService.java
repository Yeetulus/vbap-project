package com.osu.vbap.projectvbap.auth;

import com.osu.vbap.projectvbap.auth.model.AuthRequest;
import com.osu.vbap.projectvbap.auth.model.AuthResponse;
import com.osu.vbap.projectvbap.auth.model.ChangePasswordRequest;
import com.osu.vbap.projectvbap.auth.model.RegisterRequest;
import com.osu.vbap.projectvbap.exception.BadTokenFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {

    AuthResponse registerUser(RegisterRequest request);
    AuthResponse registerLibrarian(RegisterRequest request);
    void registerAdmin();
    AuthResponse authenticate(AuthRequest request);
    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, BadTokenFormatException;
    void changePassword(ChangePasswordRequest passwordRequest, HttpServletRequest request);
    void forceChangePassword(ChangePasswordRequest passwordRequest, HttpServletRequest request);

}
