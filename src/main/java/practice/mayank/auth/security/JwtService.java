package practice.mayank.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;


@Service
public class JwtService {
    private final SecretKey secretKey;
    @Value("${security.jwt.expiry_time}")
    private Long expiryTime;

    public JwtService(@Value("${security.jwt.secret_key}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return Jwts
                .builder()
                .header()
                .type("Access Token")
                .and()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(secretKey)
                .compact();
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private boolean checkExpiration(String token) {
        return extractExpiration(token).after(new Date());
    }

    // Expiration time and Token Check
    public boolean isTokenValid(String token) {
        return checkExpiration(token);
    }


}
