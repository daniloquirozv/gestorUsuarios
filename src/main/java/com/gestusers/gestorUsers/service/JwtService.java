package com.gestusers.gestorUsers.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String JWT_SECRET = "MySecretKey";
    private static final int JWT_EXPIRATION = 86400000; //Esto son 24 horas.

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        claims.put("roles", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

                return createToken(claims, userDetails.getUsername());   
    }

    private String createToken(Map<String, Object> claims, String subjeto){
        return Jwts.builder()
                .claims(claims)
                .subject(subjeto)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()), Jwts.SIG.HS512)
                .compact();
    }

    
}
