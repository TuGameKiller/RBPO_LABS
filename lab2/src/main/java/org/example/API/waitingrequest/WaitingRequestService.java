package org.example.API.waitingrequest;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WaitingRequestService {
    private final WaitingRequestRepository repository;

    public WaitingRequestService(WaitingRequestRepository repository) {
        this.repository = repository;
    }

    public List<WaitingRequestClass> findAll() {
        return repository.findAll();
    }

    public Optional<WaitingRequestClass> findById(Long id) {
        return repository.findById(id);
    }

    public WaitingRequestClass create(WaitingRequestClass waitreq) {
        // Серверная генерация даты постановки, если она не указана
        if (waitreq.getRequestDate() == null || waitreq.getRequestDate().isEmpty()) {
            waitreq.setRequestDate(LocalDate.now().toString());
        }
        // Установка дефолтного статуса, если не задан
        if (waitreq.getStatus() == null || waitreq.getStatus().isEmpty()) {
            waitreq.setStatus("PENDING");
        }
        return repository.save(waitreq);
    }

    public Optional<WaitingRequestClass> update(Long id, WaitingRequestClass updatedRequest) {
        return repository.findById(id).map(request -> {
            if (updatedRequest.getBookEditionId() != null) request.setBookEditionId(updatedRequest.getBookEditionId());
            if (updatedRequest.getUserId() != null) request.setUserId(updatedRequest.getUserId());
            if (updatedRequest.getRequestDate() != null) request.setRequestDate(updatedRequest.getRequestDate());
            if (updatedRequest.getStatus() != null) request.setStatus(updatedRequest.getStatus());
            return repository.save(request);
        });
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}