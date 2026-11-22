package org.example.API;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.function.Consumer;

public final class EntityHelper {
    private EntityHelper() {}

    public static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static <T, ID> T findOrThrow(JpaRepository<T, ID> repo, ID id, String message) {
        if (id == null) {
            throw new IllegalArgumentException(message);
        }
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException(message));
    }

    public static void updateIfPresent(String newValue, String blankError, Consumer<String> setter) {
        if (newValue != null) {
            setter.accept(requireNonBlank(newValue, blankError));
        }
    }
}