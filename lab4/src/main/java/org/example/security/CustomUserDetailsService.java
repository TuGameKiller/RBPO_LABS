package org.example.security;

import org.example.API.user.UserClass;
import org.example.API.user.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserClass user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        String role = !StringUtils.hasText(user.getRole())
                ? "READER"
                : user.getRole().trim().toUpperCase().replaceFirst("^ROLE_", "");

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!"ENABLED".equalsIgnoreCase(user.getStatus()))
                .roles(role)
                .build();
    }
}