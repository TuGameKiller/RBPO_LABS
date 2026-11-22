package org.example.security;

import org.example.API.user.UserClass;
import org.example.API.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("lib")) {
            UserClass librarian = new UserClass();
            librarian.setUsername("lib");
            librarian.setPassword(passwordEncoder.encode("lib123"));
            librarian.setRole("LIBRARIAN");
            librarian.setStatus("ENABLED");
            userRepository.save(librarian);
        }
    }
}