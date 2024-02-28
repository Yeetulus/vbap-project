package com.osu.vbap.projectvbap.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osu.vbap.projectvbap.auth.model.AuthRequest;
import com.osu.vbap.projectvbap.auth.model.AuthResponse;
import com.osu.vbap.projectvbap.auth.model.ChangePasswordRequest;
import com.osu.vbap.projectvbap.auth.model.RegisterRequest;
import com.osu.vbap.projectvbap.exception.BadTokenFormatException;
import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.exception.UserAlreadyExistsException;
import com.osu.vbap.projectvbap.jwt.JwtService;
import com.osu.vbap.projectvbap.user.Role;
import com.osu.vbap.projectvbap.user.User;
import com.osu.vbap.projectvbap.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageName;
import static com.osu.vbap.projectvbap.user.Role.ADMIN;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${security.admin}")
    private String adminPassword;

    @Override
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

    @Override
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

    @Override
    public void registerAdmin() {
        final String adminMail = "Admin@admin.com";
        var user = User.builder()
                .firstName("Admin")
                .lastName("Account")
                .email(adminMail)
                .password(passwordEncoder.encode(adminPassword))
                .role(ADMIN)
                .build();

        try {
            register(user);
            logger.info("Registering admin account");
        }
        catch (UserAlreadyExistsException e){
            logger.info("Admin account already exists");
        }
    }

    private AuthResponse register(User user) {

        if(repository.existsByEmail(user.getEmail()))
            throw new UserAlreadyExistsException(String.format("User with email %s already exists", user.getEmail()));

        var savedUser = repository.save(user);
        logger.info("New user '{}' was created", savedUser.getEmail());

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        jwtService.saveUserToken(savedUser, jwtToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userRole(user.getRole())
                .userId(user.getId())
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow(() ->
                        new ItemNotFoundException(String.format(notFoundMessageName, request.getEmail())));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        jwtService.revokeAllUserTokens(user);
        jwtService.saveUserToken(user, jwtToken);
        logger.info("""
                User {} authenticated
                Token: {}
                Refresh Token: {}""", request.getEmail(), jwtToken, refreshToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userRole(user.getRole())
                .userId(user.getId())
                .build();
    }


    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, BadTokenFormatException {
        var refreshToken = jwtService.getTokenFromRequest(request);
        var user = jwtService.getUserFromToken(refreshToken);

        if (jwtService.isTokenValid(refreshToken, user)) {
            var accessToken = jwtService.generateToken(user);
            jwtService.revokeAllUserTokens(user);
            jwtService.saveUserToken(user, accessToken);
            var authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userRole(user.getRole())
                    .userId(user.getId())
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }
    @Override
    public void changePassword(ChangePasswordRequest passwordRequest, HttpServletRequest request){
        User requestUser = repository.findByEmail(passwordRequest.getEmail()).orElseThrow(() ->
                        new ItemNotFoundException(String.format(notFoundMessageName, passwordRequest.getEmail())));
        User httpRequestUser = (User) request.getAttribute("jwtUser");

        if(!httpRequestUser.getId().equals(requestUser.getId())){
            final String message = String.format("User %s does not match with the user from token", requestUser.getEmail());
            logger.warn(message);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
        }

        changeUserPassword(passwordRequest, requestUser);
    }
    @Override
    public void forceChangePassword(ChangePasswordRequest passwordRequest, HttpServletRequest request) {
        User requestUser = repository.findByEmail(passwordRequest.getEmail()).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageName, passwordRequest.getEmail())));
        User httpRequestUser = (User) request.getAttribute("jwtUser");

        if(!httpRequestUser.getRole().equals(ADMIN)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        changePassword(passwordRequest, requestUser);
    }
    private void changeUserPassword(ChangePasswordRequest request, User user){

        String currentEncoded = passwordEncoder.encode(request.getCurrentPassword());
        if(!user.getPassword().equals(currentEncoded)){
            logger.warn("Invalid current password for user {}", user.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid current password");
        }

        changePassword(request, user);
    }

    private void changePassword(ChangePasswordRequest request, User user) {
        String newEncoded = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newEncoded);

        jwtService.revokeAllUserTokens(user);
        repository.save(user);
    }

}
