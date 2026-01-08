package practice.mayank.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import practice.mayank.auth.dto.UserRequest;
import practice.mayank.auth.dto.UserResponse;
import practice.mayank.auth.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<UserResponse> getDetail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserResponse user = userService.getUser(auth.getName()); // Authenticated user email
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserResponse updatedUser = userService.updateUser(auth.getName(), userRequest);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (userService.deleteUser(auth.getName())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
