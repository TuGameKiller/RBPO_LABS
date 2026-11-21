package org.example.API.waitingrequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/waiting-requests")
public class WaitingRequestController {
    private final WaitingRequestService service;

    public WaitingRequestController(WaitingRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<WaitingRequestClass> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaitingRequestClass> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WaitingRequestClass> create(@RequestBody WaitingRequestClass request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WaitingRequestClass> patch(@PathVariable Long id, @RequestBody WaitingRequestClass request) {
        return service.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/enqueue")
    public ResponseEntity<WaitingRequestClass> enqueue(@RequestBody EnqueueRequest request) {
        WaitingRequestClass created = service.enqueueRequest(
                request.bookEditionId, request.userId, request.requestDate
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    public static class EnqueueRequest {
        public Long bookEditionId;
        public Long userId;
        public String requestDate;
    }
}