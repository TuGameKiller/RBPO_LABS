package org.example.API.loan;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository repository;

    public LoanService(LoanRepository repository) {
        this.repository = repository;
    }

    public List<LoanClass> findAll() {
        return repository.findAll();
    }

    public Optional<LoanClass> findById(Long id) {
        return repository.findById(id);
    }

    public LoanClass create(LoanClass loan) {
        // Серверная генерация даты выдачи, если она не указана
        if (loan.getLoanDate() == null || loan.getLoanDate().isEmpty()) {
            loan.setLoanDate(LocalDate.now().toString());
        }
        return repository.save(loan);
    }

    public Optional<LoanClass> update(Long id, LoanClass updatedLoan) {
        return repository.findById(id).map(loan -> {
            if (updatedLoan.getCopyId() != null) loan.setCopyId(updatedLoan.getCopyId());
            if (updatedLoan.getUserId() != null) loan.setUserId(updatedLoan.getUserId());
            if (updatedLoan.getLoanDate() != null) loan.setLoanDate(updatedLoan.getLoanDate());
            if (updatedLoan.getReturnDate() != null) loan.setReturnDate(updatedLoan.getReturnDate());
            return repository.save(loan);
        });
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}