package com.osu.vbap.projectvbap.auth;

import com.osu.vbap.projectvbap.auth.model.AuthRequest;
import com.osu.vbap.projectvbap.auth.model.AuthResponse;
import com.osu.vbap.projectvbap.auth.model.ChangePasswordRequest;
import com.osu.vbap.projectvbap.auth.model.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login/register-user")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.registerUser(request));
    }

    @PostMapping("/login/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/admin/register-librarian")
    public ResponseEntity<AuthResponse> registerLibrarian(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.registerLibrarian(request));
    }
    @PostMapping("/user/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @PutMapping("/user/change-password")
    public ResponseEntity<Boolean> changePassword(
            @Valid @RequestBody ChangePasswordRequest request){

        return ResponseEntity.ok(service.changePassword(request));
    }

}