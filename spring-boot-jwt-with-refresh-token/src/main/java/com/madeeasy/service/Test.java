package com.madeeasy.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {
        String secretKey = "key";

        String token = Jwts.builder()
                .setId("2001214070") // id
                .setSubject("pabitra") // subject
                .setIssuer("madeeasy") // provider
                .setIssuedAt(new Date(System.currentTimeMillis())) // issue time
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1))) // exp in minute
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(secretKey.getBytes())) // encode secret key
                .compact(); // return String
        System.out.println(token);

//        String token =
//               "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyMDAxMjE0MDcwIiwic3ViIjoicGFiaXRyYSIsImlzcyI6Im1hZGVlYXN5IiwiaWF0IjoxNjc2MDM3Njk5LCJleHAiOjE2NzYwMzc3NTl9.ftauYyxldl-L93nLKhi9KocgaVRU_LVU3IRPDjz3vMo";
        Claims body = Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(secretKey.getBytes()))
                .parseClaimsJws(token)
                .getBody();
        System.out.println(body);
        System.out.println("body.getExpiration().getTime() = " + body.getExpiration().getMinutes());
    }
}
