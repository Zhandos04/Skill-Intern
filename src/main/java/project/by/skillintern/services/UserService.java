package project.by.skillintern.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import project.by.skillintern.dto.requests.UserDTO;
import project.by.skillintern.entities.User;
import project.by.skillintern.exceptions.UserAlreadyExistsException;

import java.util.Optional;

public interface UserService {
    UserDetails loadUserByUsername(String username);
    void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException;
    void update(User user);
    void removeExpiredUnverifiedUsers();

    void saveUserConfirmationCode(Long id, String code);
    void updatePassword(User user);
    boolean isUsernameTaken(String nickName);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);
}
