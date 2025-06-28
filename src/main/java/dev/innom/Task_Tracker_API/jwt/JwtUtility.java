package dev.innom.Task_Tracker_API.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtility {

    @Value("${secret}")
    private String secret;
    private SecretKey key;

    private final long expiration = 60 * 60 * 1000 * 12; // 12 hours

    @PostConstruct
    public void init(){

        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToke(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsFunction){
        return claimsFunction.apply(getClaims(token));
    }

    public boolean isExpired(String token){
        return getClaim(token, Claims::getExpiration).before(new Date());
    }

    public String getUserEmail(String token){
        return getClaim(token, Claims::getSubject);
    }
}
