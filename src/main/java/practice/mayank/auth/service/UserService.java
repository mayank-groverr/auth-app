package practice.mayank.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import practice.mayank.auth.dto.LoginRequest;
import practice.mayank.auth.dto.UserRequest;
import practice.mayank.auth.dto.UserResponse;
import practice.mayank.auth.mapper.GenericMapper;
import practice.mayank.auth.entity.Role;
import practice.mayank.auth.entity.User;
import practice.mayank.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GenericMapper genericMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserResponse getUser(String email) {
        User userInDb = findUserByEmail(email);
        return genericMapper.userToUserResponse(userInDb);
    }

    public UserResponse createNewUser(UserRequest userRequest) {
        User user = genericMapper.userRequestToUser(userRequest);
        user.getRoles().add(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        return genericMapper.userToUserResponse(newUser);
    }

    public UserResponse createNewAdmin(UserRequest userRequest) {
        User user = genericMapper.userRequestToUser(userRequest);
        user.getRoles().add(Role.USER);
        user.getRoles().add(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        return genericMapper.userToUserResponse(newUser);
    }

    public UserResponse updateUser(String email, UserRequest userRequest) {
        User user = genericMapper.userRequestToUser(userRequest);
        User userInDb = findUserByEmail(email);
        if (userInDb != null) {
            userInDb.setEmail((user.getEmail() != null && !user.getEmail().isEmpty()) ? user.getEmail() : userInDb.getEmail());
            userInDb.setName((user.getName() != null && !user.getName().isEmpty()) ? user.getName() : userInDb.getName());
            userInDb.setMobileNumber((user.getMobileNumber() != null && !user.getMobileNumber().isEmpty()) ? user.getMobileNumber() : userInDb.getMobileNumber());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userInDb.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(userInDb);
            return genericMapper.userToUserResponse(userInDb);
        }

        return null;
    }

    public boolean deleteUser(String email) {
        User user = findUserByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public User authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()));

        return findUserByEmail(loginRequest.email());

    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
