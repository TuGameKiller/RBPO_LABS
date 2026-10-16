package org.example.API.loan;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LoanRepository {
    private final List<LoanClass> storage = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<LoanClass> findAll() {
        return storage;
    }

    public Optional<LoanClass> findById(Long id) {
        return storage.stream().filter(l -> l.getId().equals(id)).findFirst();
    }

    public LoanClass save(LoanClass loan) {
        if (loan.getId() == null) {
            loan.setId(idCounter.getAndIncrement());
            storage.add(loan);
        } else {
            delete(loan.getId());
            storage.add(loan);
        }
        return loan;
    }

    public boolean delete(Long id) {
        return storage.removeIf(l -> l.getId().equals(id));
    }
}