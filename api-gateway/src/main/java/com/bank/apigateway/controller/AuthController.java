package com.bank.apigateway.controller;

import com.bank.apigateway.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.secret:ThisIsASecretKeyForJwtAuthenticationInThisAppThatIsLongEnough}")
    private String secret;

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestParam String username, @RequestParam String password) {
        // In a real application, validate username and password against a database
        if ("admin".equals(username) && "admin".equals(password)) {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());
            String token = Jwts.builder()
                    .setClaims(new HashMap<>())
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                    .signWith(key)
                    .compact();
            return Mono.just(ResponseEntity.ok(token));
        } else {
            return Mono.just(ResponseEntity.status(401).body("Invalid credentials"));
        }
    }
}
