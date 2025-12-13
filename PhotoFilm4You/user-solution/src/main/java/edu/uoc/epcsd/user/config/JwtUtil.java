package edu.uoc.epcsd.user.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import edu.uoc.epcsd.user.domain.User;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "GRUPO8_GRUPO8_GRUPO8_GRUPO8_GRUPO8";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public  String generateToken(User user, String role) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", role)
                .claim("fullName", user.getFullName())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7200000)) // 2h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
