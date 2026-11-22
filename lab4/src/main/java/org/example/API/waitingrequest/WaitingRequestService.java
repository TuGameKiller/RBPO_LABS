package org.example.API.waitingrequest;

import org.example.API.EntityHelper;
import org.example.API.bookedition.BookEditionClass;
import org.example.API.bookedition.BookEditionRepository;
import org.example.API.copy.CopyRepository;
import org.example.API.user.UserClass;
import org.example.API.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WaitingRequestService {
    private final WaitingRequestRepository repository;
    private final BookEditionRepository bookEditionRepository;
    private final UserRepository userRepository;
    private final CopyRepository copyRepository;

    public WaitingRequestService(WaitingRequestRepository repository,
                                 BookEditionRepository bookEditionRepository,
                                 UserRepository userRepository,
                                 CopyRepository copyRepository) {
        this.repository = repository;
        this.bookEditionRepository = bookEditionRepository;
        this.userRepository = userRepository;
        this.copyRepository = copyRepository;
    }

    public List<WaitingRequestClass> findAll() {
        return repository.findAll();
    }

    public Optional<WaitingRequestClass> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public WaitingRequestClass create(WaitingRequestClass request) {
        BookEditionClass edition = EntityHelper.findOrThrow(
                bookEditionRepository, request.getBookEditionId(), "Не указан или не найден bookEditionId"
        );
        UserClass user = EntityHelper.findOrThrow(
                userRepository, request.getUserId(), "Не указан или не найден userId"
        );
        request.setBookEdition(edition);
        request.setUser(user);

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            request.setStatus("PENDING");
        }
        if (request.getRequestDate() == null || request.getRequestDate().isBlank()) {
            request.setRequestDate(LocalDate.now().toString());
        }
        return repository.save(request);
    }

    @Transactional
    public Optional<WaitingRequestClass> update(Long id, WaitingRequestClass updated) {
        return repository.findById(id).map(request -> {
            EntityHelper.updateIfPresent(updated.getStatus(), "Статус заявки не может быть пустым", request::setStatus);
            EntityHelper.updateIfPresent(updated.getRequestDate(), "Дата заявки не может быть пустой", request::setRequestDate);
            return repository.save(request);
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

    // Бизнес-операция 5: постановка в очередь ожидания на издание
    @Transactional
    public WaitingRequestClass enqueueRequest(Long bookEditionId, Long userId, String requestDate) {
        BookEditionClass edition = EntityHelper.findOrThrow(
                bookEditionRepository, bookEditionId, "Не указан или не найден bookEditionId"
        );
        UserClass user = EntityHelper.findOrThrow(
                userRepository, userId, "Не указан или не найден userId"
        );

        if ("DISABLED".equalsIgnoreCase(user.getStatus())) {
            throw new IllegalStateException("Невозможно встать в очередь: пользователь отключён (DISABLED)");
        }

        boolean hasAvailableCopy = copyRepository.findAll().stream()
                .anyMatch(copy -> copy.getBookEdition() != null
                        && edition.getId().equals(copy.getBookEdition().getId())
                        && "AVAILABLE".equalsIgnoreCase(copy.getStatus()));
        if (hasAvailableCopy) {
            throw new IllegalStateException("Невозможно встать в очередь: есть свободный экземпляр издания");
        }

        boolean alreadyInQueue = repository.findAll().stream()
                .anyMatch(wr -> wr.getBookEdition() != null
                        && edition.getId().equals(wr.getBookEdition().getId())
                        && wr.getUser() != null
                        && user.getId().equals(wr.getUser().getId())
                        && "PENDING".equalsIgnoreCase(wr.getStatus()));
        if (alreadyInQueue) {
            throw new IllegalStateException("Пользователь уже состоит в активной очереди на это издание");
        }

        WaitingRequestClass request = new WaitingRequestClass();
        request.setBookEdition(edition);
        request.setUser(user);
        request.setStatus("PENDING");
        request.setRequestDate((requestDate == null || requestDate.isBlank())
                ? LocalDate.now().toString()
                : requestDate);

        return repository.save(request);
    }
}