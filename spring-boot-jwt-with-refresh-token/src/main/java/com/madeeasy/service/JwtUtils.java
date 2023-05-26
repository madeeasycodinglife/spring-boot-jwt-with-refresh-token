package com.madeeasy.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private static final String secretKey = "SECRET_KEY";

    public String generateToken(String email) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString()) // id
                .setSubject(email) // email
                .setIssuer("madeeasy") // provider
                .setIssuedAt(new Date(System.currentTimeMillis())) // issue time
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30))) // exp in minute
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(secretKey.getBytes())) // encode secret key
                .compact(); // return String
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(secretKey.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    public String getUserName(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDate(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    public boolean validateToken(String token, String name) {
        String userName = getUserName(token);
        return userName.equals(name) && !isTokenExpired(token);
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
}
















