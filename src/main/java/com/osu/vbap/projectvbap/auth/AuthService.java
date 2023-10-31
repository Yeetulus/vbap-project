package com.osu.vbap.projectvbap.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osu.vbap.projectvbap.auth.model.AuthRequest;
import com.osu.vbap.projectvbap.auth.model.AuthResponse;
import com.osu.vbap.projectvbap.auth.model.ChangePasswordRequest;
import com.osu.vbap.projectvbap.auth.model.RegisterRequest;
import com.osu.vbap.projectvbap.jwt.JwtService;
import com.osu.vbap.projectvbap.jwt.Token;
import com.osu.vbap.projectvbap.jwt.TokenRepository;
import com.osu.vbap.projectvbap.user.Role;
import com.osu.vbap.projectvbap.user.User;
import com.osu.vbap.projectvbap.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${security.admin}")
    private String adminPassword;

    public AuthResponse registerUser(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .build();
        return register(user);
    }

    public AuthResponse registerLibrarian(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.LIBRARIAN)
                .build();
        return register(user);
    }

    public void registerAdmin()
    {
        var user = User.builder()
                .firstName("Admin")
                .lastName("Account")
                .email("Admin@admin.com")
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build();

        if(repository.findByEmail(user.getEmail()).isEmpty()) {
            logger.info("Creating admin account");
            register(user);
        }
        else logger.info("Admin account already exists");
    }

    private AuthResponse register(User user) {
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        logger.info("New user '{}' was created", savedUser.getEmail());
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        logger.info("""
                User {} authenticated
                Token: {}
                Refresh Token: {}""", request.getEmail(), jwtToken, refreshToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllByUser_IdAndExpiredFalseAndRevokedFalse(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
        logger.info("Revoked all tokens of user {}", user.getEmail());
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public boolean changePassword(ChangePasswordRequest request){
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        String currentEncoded = passwordEncoder.encode(request.getCurrentPassword());

        if(!user.getPassword().equals(currentEncoded)){
            logger.warn("Invalid current password for user {}", user.getEmail());
            throw new RuntimeException("Invalid current password");
        }

        String newEncoded = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newEncoded);

        repository.save(user);
        revokeAllUserTokens(user);

        return true;
    }

}
