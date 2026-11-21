package org.example.API.loan;

import org.example.API.EntityHelper;
import org.example.API.copy.CopyClass;
import org.example.API.copy.CopyRepository;
import org.example.API.user.UserClass;
import org.example.API.user.UserRepository;
import org.example.API.waitingrequest.WaitingRequestClass;
import org.example.API.waitingrequest.WaitingRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository repository;
    private final CopyRepository copyRepository;
    private final UserRepository userRepository;
    private final WaitingRequestRepository waitingRequestRepository;

    public LoanService(LoanRepository repository,
                       CopyRepository copyRepository,
                       UserRepository userRepository,
                       WaitingRequestRepository waitingRequestRepository) {
        this.repository = repository;
        this.copyRepository = copyRepository;
        this.userRepository = userRepository;
        this.waitingRequestRepository = waitingRequestRepository;
    }

    public List<LoanClass> findAll() {
        return repository.findAll();
    }

    public Optional<LoanClass> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public LoanClass create(LoanClass loan) {
        CopyClass copy = EntityHelper.findOrThrow(
                copyRepository, loan.getCopyId(), "Не указан или не найден copyId"
        );
        UserClass user = EntityHelper.findOrThrow(
                userRepository, loan.getUserId(), "Не указан или не найден userId"
        );
        loan.setCopy(copy);
        loan.setUser(user);

        if (loan.getLoanDate() == null || loan.getLoanDate().isBlank()) {
            loan.setLoanDate(LocalDate.now().toString());
        }
        return repository.save(loan);
    }

    @Transactional
    public Optional<LoanClass> update(Long id, LoanClass updated) {
        return repository.findById(id).map(loan -> {
            if (updated.getCopyId() != null) {
                CopyClass copy = EntityHelper.findOrThrow(
                        copyRepository, updated.getCopyId(), "Экземпляр с ID " + updated.getCopyId() + " не найден"
                );
                loan.setCopy(copy);
            }
            if (updated.getUserId() != null) {
                UserClass user = EntityHelper.findOrThrow(
                        userRepository, updated.getUserId(), "Пользователь с ID " + updated.getUserId() + " не найден"
                );
                loan.setUser(user);
            }
            EntityHelper.updateIfPresent(updated.getLoanDate(), "loanDate не может быть пустым", loan::setLoanDate);
            if (updated.getReturnDate() != null) {
                loan.setReturnDate(updated.getReturnDate());
            }
            return repository.save(loan);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // Бизнес-операция 1: выдача экземпляра читателю
    @Transactional
    public LoanClass issueCopy(Long copyId, Long userId, String loanDate) {
        CopyClass copy = EntityHelper.findOrThrow(
                copyRepository, copyId, "Не указан или не найден copyId"
        );
        if (!"AVAILABLE".equalsIgnoreCase(copy.getStatus())) {
            throw new IllegalStateException("Невозможно выдать экземпляр: текущий статус " + copy.getStatus());
        }

        UserClass user = EntityHelper.findOrThrow(
                userRepository, userId, "Не указан или не найден userId"
        );
        if ("DISABLED".equalsIgnoreCase(user.getStatus())) {
            throw new IllegalStateException("Невозможно выдать книгу: пользователь отключён (DISABLED)");
        }

        copy.setStatus("BORROWED");
        copyRepository.save(copy);

        LoanClass loan = new LoanClass();
        loan.setCopy(copy);
        loan.setUser(user);
        loan.setLoanDate((loanDate == null || loanDate.isBlank()) ? LocalDate.now().toString() : loanDate);
        return repository.save(loan);
    }

    // Бизнес-операция 2: возврат экземпляра с авто-передачей по очереди
    @Transactional
    public LoanClass returnCopy(Long loanId, String returnDate) {
        LoanClass loan = EntityHelper.findOrThrow(
                repository, loanId, "Выдача с ID " + loanId + " не найдена"
        );
        if (loan.getReturnDate() != null && !loan.getReturnDate().isBlank()) {
            throw new IllegalStateException("Выдача с ID " + loanId + " уже закрыта");
        }

        String effectiveReturnDate = (returnDate == null || returnDate.isBlank())
                ? LocalDate.now().toString()
                : returnDate;
        loan.setReturnDate(effectiveReturnDate);
        repository.save(loan);

        CopyClass copy = loan.getCopy();
        Long editionId = (copy.getBookEdition() != null) ? copy.getBookEdition().getId() : copy.getBookEditionId();

        Optional<WaitingRequestClass> pendingRequestOpt = (editionId == null)
                ? Optional.empty()
                : waitingRequestRepository.findAll().stream()
                .filter(wr -> wr.getBookEditionId() != null
                        && wr.getBookEditionId().equals(editionId)
                        && "PENDING".equalsIgnoreCase(wr.getStatus()))
                .findFirst();

        if (pendingRequestOpt.isPresent()) {
            WaitingRequestClass request = pendingRequestOpt.get();
            UserClass nextUser = EntityHelper.findOrThrow(
                    userRepository, request.getUserId(), "Пользователь из очереди с ID " + request.getUserId() + " не найден"
            );

            request.setStatus("FULFILLED");
            waitingRequestRepository.save(request);

            copy.setStatus("BORROWED");
            copyRepository.save(copy);

            LoanClass nextLoan = new LoanClass();
            nextLoan.setCopy(copy);
            nextLoan.setUser(nextUser);
            nextLoan.setLoanDate(effectiveReturnDate);
            repository.save(nextLoan);
        } else {
            copy.setStatus("AVAILABLE");
            copyRepository.save(copy);
        }

        return loan;
    }

    // Бизнес-операция 3: продление срока выдачи
    @Transactional
    public LoanClass extendLoan(Long loanId, String newDate) {
        LoanClass loan = EntityHelper.findOrThrow(
                repository, loanId, "Выдача с ID " + loanId + " не найдена"
        );
        if (loan.getReturnDate() != null && !loan.getReturnDate().isBlank()) {
            throw new IllegalStateException("Невозможно продлить уже закрытую выдачу");
        }

        CopyClass copy = loan.getCopy();
        Long editionId = (copy != null && copy.getBookEdition() != null)
                ? copy.getBookEdition().getId()
                : (copy != null ? copy.getBookEditionId() : null);

        if (editionId != null) {
            boolean hasPendingQueue = waitingRequestRepository.findAll().stream()
                    .anyMatch(wr -> wr.getBookEditionId() != null
                            && wr.getBookEditionId().equals(editionId)
                            && "PENDING".equalsIgnoreCase(wr.getStatus()));
            if (hasPendingQueue) {
                throw new IllegalStateException("Невозможно продлить выдачу: на это издание есть активная очередь ожидания");
            }
        }

        String updatedDate = (newDate == null || newDate.isBlank())
                ? LocalDate.now().toString()
                : newDate;
        loan.setLoanDate(updatedDate);
        return repository.save(loan);
    }
}