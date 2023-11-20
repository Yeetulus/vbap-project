package com.osu.vbap.projectvbap.jwt;

import com.osu.vbap.projectvbap.exception.BadTokenFormatException;
import com.osu.vbap.projectvbap.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    void saveUserToken(User user, String jwtToken);
    void revokeAllUserTokens(User user);
    User getUserFromRequest(HttpServletRequest request);
    User getUserFromToken(String token);
    String getTokenFromRequest(HttpServletRequest request);
}
