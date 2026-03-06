package practice.mayank.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.mayank.auth.dto.*;
import practice.mayank.auth.entity.User;
import practice.mayank.auth.security.SecurityTokenService;
import practice.mayank.auth.service.UserService;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;
    private final SecurityTokenService securityTokenService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest) {
        UserResponse newUser = userService.createNewUser(userRequest);
        if (newUser != null) {
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Request with Credentials -> Verify -> Return token if valid
    @PostMapping("/login")
    public ResponseEntity<SecurityTokenResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User user = userService.authenticate(loginRequest);
        SecurityTokenResponse securityTokenResponse = securityTokenService.generateSecurityToken(user, response);
        return new ResponseEntity<>(securityTokenResponse, HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<SecurityTokenResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SecurityTokenResponse refreshTokenResponse =
                securityTokenService.renewAccessTokenAndRefreshToken(request, response);
        return new ResponseEntity<>(refreshTokenResponse, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        securityTokenService.logoutUser(request, response);
        return ResponseEntity.noContent().build();
    }
}
