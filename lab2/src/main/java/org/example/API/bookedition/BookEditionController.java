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

    // чтение всех — 200 и JSON массива
    @GetMapping
    public List<BookEditionClass> getAll() {
        return service.findAll();
    }

    // чтение по id — 200 и JSON ресурса
    @GetMapping("/{id}")
    public ResponseEntity<BookEditionClass> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // создание — 201 и JSON созданного ресурса с id
    @PostMapping
    public ResponseEntity<BookEditionClass> create(@RequestBody BookEditionClass book) {
        BookEditionClass created = service.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // обновление — 200 и JSON ресурса
    @PatchMapping("/{id}")
    public ResponseEntity<BookEditionClass> update(@PathVariable Long id, @RequestBody BookEditionClass book) {
        return service.update(id, book)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // удаление — 204
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}