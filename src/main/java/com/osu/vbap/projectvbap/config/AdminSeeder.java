package com.osu.vbap.projectvbap.config;

import com.osu.vbap.projectvbap.auth.AuthService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminSeeder {

    private final AuthService authService;

    @PostConstruct
    public void seedAdmin() {
        authService.registerAdmin();
    }
}