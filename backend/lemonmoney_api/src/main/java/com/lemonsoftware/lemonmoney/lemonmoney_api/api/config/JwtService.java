package com.lemonsoftware.lemonmoney.lemonmoney_api.api.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String SECRET = "12345678901234567890123456789012"; // 32 chars

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private final long accessTokenExpirationMs = 30 * 60 * 1000; // 30 minutos
    private final long refreshTokenExpirationMs = 7 * 24 * 60 * 60 * 1000; // 7 dias

    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenExpirationMs);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenExpirationMs);
    }

        private String generateToken(String username, long expirationMs) {
            return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256) // assinatura explícita com algoritmo
                .compact();
        }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
        return expiration.before(new Date());
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
