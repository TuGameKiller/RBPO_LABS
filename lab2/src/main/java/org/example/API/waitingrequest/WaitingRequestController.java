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
    public ResponseEntity<WaitingRequestClass> create(@RequestBody WaitingRequestClass waitreq) {
        WaitingRequestClass created = service.create(waitreq);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WaitingRequestClass> update(@PathVariable Long id, @RequestBody WaitingRequestClass waitreq) {
        return service.update(id, waitreq)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}