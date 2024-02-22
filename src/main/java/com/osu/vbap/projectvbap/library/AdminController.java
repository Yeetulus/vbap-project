package com.osu.vbap.projectvbap.library;


import com.osu.vbap.projectvbap.auth.AuthService;
import com.osu.vbap.projectvbap.auth.model.AuthResponse;
import com.osu.vbap.projectvbap.auth.model.ChangePasswordRequest;
import com.osu.vbap.projectvbap.auth.model.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AuthService service;

    @Operation(
            description = "Register librarian",
            summary = "Registers new librarian account from request",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Account email already used",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping("/register-librarian")
    public ResponseEntity<AuthResponse> registerLibrarian(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerLibrarian(request));
    }

    @Operation(
            description = "Force change password",
            summary = "Changes password for any account",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @PutMapping("/change-password")
    public ResponseEntity<Void> forceChangePassword(
            @Valid @RequestBody ChangePasswordRequest passwordRequest,
            HttpServletRequest request){

        service.forceChangePassword(passwordRequest, request);
        return ResponseEntity.ok().build();
    }
}
