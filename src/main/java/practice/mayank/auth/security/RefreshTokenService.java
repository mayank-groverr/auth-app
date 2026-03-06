package practice.mayank.auth.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.mayank.auth.entity.RefreshToken;
import practice.mayank.auth.entity.User;
import practice.mayank.auth.exception.customexception.ResourceNotFoundException;
import practice.mayank.auth.exception.customexception.TokenExpiredException;
import practice.mayank.auth.repository.RefreshTokenRepository;
import practice.mayank.auth.service.UserService;


import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Getter
    @Value("${security.jwt.refresh_token.expiry_time}")
    private Long refreshTokenDuration;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;




    public String createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plus(Duration.ofMinutes(refreshTokenDuration)));
        RefreshToken newRefreshToken = refreshTokenRepository.save(token);
        return newRefreshToken.getToken();
    }


    public String renewRefreshToken(RefreshToken refreshToken){
        if(isTokenExpired(refreshToken)){
            deleteRefreshToken(refreshToken);
            throw new TokenExpiredException("Token Expired");
        }
        deleteRefreshToken(refreshToken);
        return createRefreshToken(refreshToken.getUser());
    }

    @Transactional
    public void invalidateRefreshToken(String refreshTokenFromRequest){
        RefreshToken refreshToken = findRefreshToken(refreshTokenFromRequest);
        deleteRefreshToken(refreshToken);
    }

    private boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }

    public RefreshToken findRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken.orElseThrow(() -> new ResourceNotFoundException("Invalid token: " + token));
    }


    private void deleteRefreshToken(RefreshToken refreshToken){
        refreshTokenRepository.delete(refreshToken);
    }



}
