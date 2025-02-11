package com.kokolushkin.TaskManager.component;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public String generateToken(String username) {
        return Jwts.builder()
                   .subject(username)
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + 3600000))
                   .signWith(secretKey)
                   .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getExpiration()
                   .before(new Date());
    }
}
