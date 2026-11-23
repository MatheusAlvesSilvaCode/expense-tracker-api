package com.roadmap.expense_tracker_api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET_KEY = "minha-chave-seceta-ultra-grande-pois-o-jwt-e-bem-chato-com-isso-coisa-irritante";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String email) {
        long expirationTimeMillis = 1000L * 60 * 60; // Token expira em 1h

        return Jwts.builder()
                .subject(email) // guarda email do usuario dentro do token
                .issuedAt(new Date()) // Marca a data e hora exatas em que o token foi emitido
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMillis)) //Define quando otoken vai expirar
                .signWith(getSigningKey()) // assina o token usando a chave secreta.
                .compact();// Converte tudo isso em um string.
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenExpired(String token, String email) {
        String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token, email));
    }

    private boolean isTokenExpired(String token){
        Date expiration = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }
}
