package com.queuemgmt.auth.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-ms")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration-ms")
    private long refreshTokenExpiration;
   

    /*Method to signing the key */
    private SecretKey getSigningKey(){

        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    /* Method to generate the access token */
    public String generateAccessToken(UUID userId, String email, String role){

        Date now  = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .claims(Map.of(
                    "email", email,
                    "role", role,
                    "type", "access"
                ))
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
        
    }
    
    /* Method to generate the refresh token  */

    public String generateRefreshToken(UUID userId){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpiration);
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .claims(Map.of(
                    "type", "refresh"
                ))
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }


    /* Method to validate the token  */
    public Claims validateToken(String token){

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
 