package com.lemonsoftware.lemonmoney.lemonmoney_api.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        
        UserDetails user = userDetailsService.loadUserByUsername(username);
        PasswordEncoder encoder = passwordEncoder();
        

        if (encoder.matches(password, user.getPassword())) {
            String accessToken = jwtService.generateAccessToken(username);
            String refreshToken = jwtService.generateRefreshToken(username);

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);         
            refreshTokenCookie.setSecure(true);            
            refreshTokenCookie.setPath("/auth/refresh");                
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); 

            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        }
        return ResponseEntity.status(401).body("Usuário ou senha inválidos");
    }

    public static class AuthResponse {
        private String accessToken;
        private String refreshToken;

        public AuthResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        try {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String newAccessToken = jwtService.generateAccessToken(username);
                return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
            }

            return ResponseEntity.status(401).body("Refresh token inválido ou expirado");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Refresh token inválido");
        }
    }
    

    private PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
