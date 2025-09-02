package com.gestusers.gestorUsers.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
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

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token) );
    }



    
}
