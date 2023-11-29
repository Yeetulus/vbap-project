package com.osu.vbap.projectvbap.library;


import com.osu.vbap.projectvbap.auth.AuthService;
import com.osu.vbap.projectvbap.auth.model.AuthResponse;
import com.osu.vbap.projectvbap.auth.model.ChangePasswordRequest;
import com.osu.vbap.projectvbap.auth.model.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService service;
    @PostMapping("/register-librarian")
    public ResponseEntity<AuthResponse> registerLibrarian(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerLibrarian(request));
    }
    @PutMapping("/change-password")
    public ResponseEntity<Void> forceChangePassword(
            @Valid @RequestBody ChangePasswordRequest passwordRequest,
            HttpServletRequest request){

        service.forceChangePassword(passwordRequest, request);
        return ResponseEntity.ok().build();
    }
}
