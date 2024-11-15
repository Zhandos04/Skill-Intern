package project.by.skillintern.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.by.skillintern.dto.requests.UserDTO;
import project.by.skillintern.entities.Role;
import project.by.skillintern.entities.User;
import project.by.skillintern.exceptions.UserAlreadyExistsException;
import project.by.skillintern.repositories.UserRepository;
import project.by.skillintern.services.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername1(), user.getPassword(), user.getAuthorities());
    }


    @Transactional
    @Override
    public void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("A user with that email already exists");
        }
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UserAlreadyExistsException("A user with that username already exists");
        }
        User user = convertToUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsVerified(false);
        if (Boolean.TRUE.equals(userDTO.getIsEmployer())) {
            user.setRole(Role.ROLE_EMPLOYER);
        } else {
            user.setRole(Role.ROLE_USER);
        }
        userRepository.save(user);
        String code = generateCode();
        saveUserConfirmationCode(user.getId(), code);
        try {
            emailService.sendEmail(userDTO.getEmail(), "Skill Intern Verity Email", "Your code is: " + code);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send confirmation email", e);
        }

    }
    @Transactional
    @Override
    public void update(User user){
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }

    @Override
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void removeExpiredUnverifiedUsers() {
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(24);
        userRepository.deleteExpiredUnverifiedUsers(expirationTime);
    }

    @Transactional
    @Override
    public void saveUserConfirmationCode(Long id, String code) {
        User user = userRepository.getUserById(id);
        user.setConfirmationCode(code);
        user.setCodeSentAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updatePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameTaken(String nickName) {
        return userRepository.existsByUsername(nickName);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private String generateCode() {
        return Integer.toString((int)(Math.random() * 900000) + 100000);
    }
}
