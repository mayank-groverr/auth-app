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

    public JwtService(@Value("${security.jwt.secret_key}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return Jwts
                .builder()
                .header()
                .type("Jwt")
                .and()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(secretKey)
                .compact();
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    private Date extractExpiration(String token) {
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
