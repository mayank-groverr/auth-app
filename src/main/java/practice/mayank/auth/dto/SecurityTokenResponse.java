package practice.mayank.auth.dto;


import java.util.Date;


public record SecurityTokenResponse(
        String accessToken,
        Date accessTokenExpiryTime
) {
}
