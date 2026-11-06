package org.example.API.waitingrequest;

import org.example.API.bookedition.BookEditionClass;
import org.example.API.bookedition.BookEditionRepository;
import org.example.API.user.UserClass;
import org.example.API.user.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WaitingRequestService {
    private final WaitingRequestRepository repository;
    private final BookEditionRepository bookEditionRepository;
    private final UserRepository userRepository;

    public WaitingRequestService(WaitingRequestRepository repository,
                                 BookEditionRepository bookEditionRepository,
                                 UserRepository userRepository) {
        this.repository = repository;
        this.bookEditionRepository = bookEditionRepository;
        this.userRepository = userRepository;
    }

    public List<WaitingRequestClass> findAll() { return repository.findAll(); }
    public Optional<WaitingRequestClass> findById(Long id) { return repository.findById(id); }

    public WaitingRequestClass create(WaitingRequestClass request) {
        if (request.getBookEditionId() != null) {
            BookEditionClass edition = bookEditionRepository.findById(request.getBookEditionId())
                    .orElseThrow(() -> new RuntimeException("Книжное издание с ID " + request.getBookEditionId() + " не найдено"));
            request.setBookEdition(edition);
        } else {
            throw new RuntimeException("Не указан bookEditionId");
        }

        if (request.getUserId() != null) {
            UserClass user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + request.getUserId() + " не найден"));
            request.setUser(user);
        } else {
            throw new RuntimeException("Не указан userId");
        }

        if (request.getStatus() == null || request.getStatus().isEmpty()) {
            request.setStatus("PENDING");
        }
        if (request.getRequestDate() == null || request.getRequestDate().isEmpty()) {
            request.setRequestDate(LocalDate.now().toString());
        }

        return repository.save(request);
    }

    public Optional<WaitingRequestClass> update(Long id, WaitingRequestClass updated) {
        return repository.findById(id).map(request -> {
            if (updated.getStatus() != null) {
                request.setStatus(updated.getStatus());
            }
            return repository.save(request);
        });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}