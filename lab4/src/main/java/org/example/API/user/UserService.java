package org.example.API.user;

import org.example.API.EntityHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserClass> findAll() {
        return repository.findAll();
    }

    public Optional<UserClass> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public UserClass create(UserClass user) {
        String username = EntityHelper.requireNonBlank(user.getUsername(), "Не указан username пользователя");
        EntityHelper.requireNonBlank(user.getPassword(), "Не указан password пользователя");

        if (repository.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с username '" + username + "' уже существует");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("READER");
        }
        if (user.getStatus() == null || user.getStatus().isBlank()) {
            user.setStatus("ENABLED");
        }
        return repository.save(user);
    }

    @Transactional
    public Optional<UserClass> update(Long id, UserClass updatedUser) {
        return repository.findById(id).map(user -> {
            EntityHelper.updateIfPresent(updatedUser.getUsername(), "username не может быть пустым", val -> {
                if (repository.existsByUsernameAndIdNot(val, id)) {
                    throw new IllegalArgumentException("Пользователь с username '" + val + "' уже существует");
                }
                user.setUsername(val);
            });
            EntityHelper.updateIfPresent(updatedUser.getPassword(), "password не может быть пустым", user::setPassword);
            EntityHelper.updateIfPresent(updatedUser.getRole(), "role не может быть пустой", user::setRole);
            EntityHelper.updateIfPresent(updatedUser.getStatus(), "Состояние пользователя не может быть пустым", user::setStatus);
            return repository.save(user);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}