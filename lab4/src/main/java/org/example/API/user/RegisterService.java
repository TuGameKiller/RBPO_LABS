package org.example.API.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserClass registerUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует!");
        }


        if (rawPassword == null || rawPassword.length() < 8 ||
                !rawPassword.matches(".*[0-9].*") || !rawPassword.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 8 символов, хотя бы одну цифру и букву!");
        }

        UserClass user = new UserClass();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("ROLE_USER"); // При регистрации выдаем базовую роль
        user.setEnabled(true);

        return userRepository.save(user);
    }
}