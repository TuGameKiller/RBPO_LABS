package org.example.API.user;

import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserClass register(String username, String rawPassword) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Имя пользователя не должно быть пустым");
        }
        String cleanUsername = username.trim();
        if (userRepository.existsByUsername(cleanUsername)) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        if (!StringUtils.hasText(rawPassword)
                || rawPassword.length() < 8
                || !rawPassword.matches(".*[A-Za-z].*")
                || !rawPassword.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Пароль должен быть не короче 8 символов и содержать букву и цифру");
        }

        UserClass user = new UserClass();
        user.setUsername(cleanUsername);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("READER");
        user.setStatus("ENABLED");

        return userRepository.save(user);
    }
}

