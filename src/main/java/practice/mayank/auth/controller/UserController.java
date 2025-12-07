package practice.mayank.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.mayank.auth.entity.User;
import practice.mayank.auth.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {


    private final UserService userService;


    @GetMapping("/{email}")
    public ResponseEntity<User> getdetail(@PathVariable String email) {
        User user = userService.getUser(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping({"/{email}"})
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User user) {
        if (user != null) {
            User updatedUser = userService.updateUser(email, user);
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
