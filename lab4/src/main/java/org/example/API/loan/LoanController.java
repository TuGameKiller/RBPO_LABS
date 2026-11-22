package org.example.API.loan;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @GetMapping
    public List<LoanClass> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanClass> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<LoanClass> create(@RequestBody LoanClass loan) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(loan));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<LoanClass> patch(@PathVariable Long id, @RequestBody LoanClass loan) {
        return service.update(id, loan)
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

    @PostMapping("/issue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<LoanClass> issue(@RequestBody IssueRequest request) {
        LoanClass created = service.issueCopy(request.copyId, request.userId, request.loanDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<LoanClass> returnLoan(@PathVariable Long id,
                                                @RequestBody(required = false) DateRequest request) {
        String returnDate = request != null ? request.date : null;
        return ResponseEntity.ok(service.returnCopy(id, returnDate));
    }

    @PostMapping("/{id}/extend")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN')")
    public ResponseEntity<LoanClass> extend(@PathVariable Long id,
                                            @RequestBody(required = false) DateRequest request) {
        String newDate = request != null ? request.date : null;
        return ResponseEntity.ok(service.extendLoan(id, newDate));
    }

    public static class IssueRequest {
        public Long copyId;
        public Long userId;
        public String loanDate;
    }

    public static class DateRequest {
        public String date;
    }
}