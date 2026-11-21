package org.example.API.bookedition;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/book-editions")
public class BookEditionController {
    private final BookEditionService service;

    public BookEditionController(BookEditionService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookEditionClass> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookEditionClass> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookEditionClass> create(@RequestBody BookEditionClass edition) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(edition));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookEditionClass> patch(@PathVariable Long id, @RequestBody BookEditionClass edition) {
        return service.update(id, edition)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}