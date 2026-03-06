package practice.mayank.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import practice.mayank.auth.exception.customexception.BadRequestException;

import java.util.Arrays;


@Service
public class CookieService {
    private final String refreshTokenCookieName;
    private final String cookieSameSite;
    private final String cookieDomain;

    public CookieService(
            @Value("${security.jwt.refresh_token.refresh_Token_Cookie_Name}") String refreshTokenCookieName,
            @Value("${security.jwt.refresh_token.cookie_same_site}") String cookieSameSite,
            @Value("${security.jwt.refresh_token.cookie_domain}") String cookieDomain
    ) {
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.cookieSameSite = cookieSameSite;
        this.cookieDomain = cookieDomain;
    }

    public void attachRefreshTokenToCookie(HttpServletResponse response, String refreshToken, Long maxAge){
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(refreshTokenCookieName, refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAge)
                .sameSite(cookieSameSite);

        if(cookieDomain != null && !cookieDomain.isBlank()){
            responseCookieBuilder.domain(cookieDomain);
        }

        ResponseCookie responseCookie = responseCookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }


    public void clearRefreshCookie(HttpServletResponse response){
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(refreshTokenCookieName, "")
                .maxAge(0)
                .path("/")
                .sameSite(cookieSameSite)
                .httpOnly(true)
                .secure(true);

        if(cookieDomain != null && !cookieDomain.isBlank()){
            responseCookieBuilder.domain(cookieDomain);
        }

        ResponseCookie responseCookie = responseCookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public String readRefreshTokenFromRequest(HttpServletRequest request){
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals(refreshTokenCookieName)){
                    return cookie.getValue();
                }
            }
        }
        throw new BadRequestException("Refresh Token Missing");
    }
}
