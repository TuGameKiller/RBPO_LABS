package org.example.security;

import org.example.API.user.UserClass;
import org.example.API.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        boolean enabled = "ENABLED".equalsIgnoreCase(user.getStatus());
        String role = (user.getRole() == null || user.getRole().isBlank())
                ? "READER"
                : user.getRole().trim().toUpperCase().replaceFirst("^ROLE_", "");

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!enabled)
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)))
                .build();
    }
}