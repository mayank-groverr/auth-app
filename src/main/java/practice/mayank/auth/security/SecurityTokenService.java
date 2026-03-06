package practice.mayank.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.mayank.auth.dto.SecurityTokenResponse;
import practice.mayank.auth.entity.RefreshToken;
import practice.mayank.auth.entity.User;
import practice.mayank.auth.service.UserService;


@Service
@RequiredArgsConstructor
public class SecurityTokenService {

    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Transactional
    public SecurityTokenResponse generateSecurityToken(User user, HttpServletResponse response){
        String accessToken = jwtService.generateToken(user.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(user);
        cookieService.attachRefreshTokenToCookie(response, refreshToken, refreshTokenService.getRefreshTokenDuration());
        return new SecurityTokenResponse(accessToken, jwtService.extractExpiration(accessToken));
    }

    @Transactional
    public void logoutUser(HttpServletRequest request, HttpServletResponse response){
        String refreshTokenFromRequest = cookieService.readRefreshTokenFromRequest(request);
        refreshTokenService.invalidateRefreshToken(refreshTokenFromRequest);
        cookieService.clearRefreshCookie(response);
    }

    @Transactional
    public SecurityTokenResponse renewAccessTokenAndRefreshToken(
            HttpServletRequest request,
            HttpServletResponse response
            ){
        String refreshTokenFromRequest = cookieService.readRefreshTokenFromRequest(request);
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenFromRequest);
        String renewedRefreshToken = refreshTokenService.renewRefreshToken(refreshToken);
        String renewedJwtToken = jwtService.generateToken(refreshToken.getUser().getEmail());
        cookieService.attachRefreshTokenToCookie(response, renewedRefreshToken, refreshTokenService.getRefreshTokenDuration());
        return new SecurityTokenResponse(renewedJwtToken, jwtService.extractExpiration(renewedJwtToken));
    }


}
