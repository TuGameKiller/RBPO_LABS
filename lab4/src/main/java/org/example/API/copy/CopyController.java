package org.example.API.copy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/copies")
public class CopyController {
    private final CopyService service;

    public CopyController(CopyService service) {
        this.service = service;
    }

    @GetMapping
    public List<CopyClass> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CopyClass> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<CopyClass> create(@RequestBody CopyClass copy) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(copy));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<CopyClass> patch(@PathVariable Long id, @RequestBody CopyClass copy) {
        return service.update(id, copy)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/write-off")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<CopyClass> writeOff(@PathVariable Long id) {
        return ResponseEntity.ok(service.writeOffCopy(id));
    }
}