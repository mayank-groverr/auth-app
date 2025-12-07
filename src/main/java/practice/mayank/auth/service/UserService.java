package practice.mayank.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.mayank.auth.entity.User;
import practice.mayank.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User getUser(String email) {
        return findUserByEmail(email);
    }

    public User createNewUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String email, User user) {
        User userInDb = findUserByEmail(email);
        if (userInDb != null) {
            userInDb.setEmail((user.getEmail() != null && !user.getEmail().isEmpty()) ? user.getEmail() : userInDb.getEmail());
            userInDb.setName((user.getName() != null && !user.getName().isEmpty()) ? user.getName() : userInDb.getName());
            userInDb.setMobileNumber((user.getMobileNumber() != null && !user.getMobileNumber().isEmpty()) ? user.getMobileNumber() : userInDb.getMobileNumber());
            userInDb.setPassword((user.getPassword() != null && !user.getPassword().isEmpty()) ? user.getPassword() : userInDb.getPassword());
            userRepository.save(userInDb);
            return userInDb;
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

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
