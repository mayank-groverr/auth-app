package practice.mayank.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.mayank.auth.dto.UserRequest;
import practice.mayank.auth.dto.UserResponse;
import practice.mayank.auth.entity.User;
import practice.mayank.auth.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {


    private final UserService userService;


    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getdetail(@PathVariable String email) {
        UserResponse user = userService.getUser(email);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping({"/{email}"})
    public ResponseEntity<UserResponse> updateUser(@PathVariable String email, @RequestBody UserRequest userRequest) {
            UserResponse updatedUser = userService.updateUser(email, userRequest);
            if (updatedUser != null){
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String email) {
        if (userService.deleteUser(email)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
