package org.example.API.loan;

import org.example.API.copy.CopyClass;
import org.example.API.copy.CopyRepository;
import org.example.API.user.UserClass;
import org.example.API.user.UserRepository;
import org.example.API.waitingrequest.WaitingRequestClass;
import org.example.API.waitingrequest.WaitingRequestRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository repository;
    private final CopyRepository copyRepository;
    private final UserRepository userRepository;
    private final WaitingRequestRepository waitingRequestRepository;

    public LoanService(LoanRepository repository, CopyRepository copyRepository,
                       UserRepository userRepository, WaitingRequestRepository waitingRequestRepository) {
        this.repository = repository;
        this.copyRepository = copyRepository;
        this.userRepository = userRepository;
        this.waitingRequestRepository = waitingRequestRepository;
    }

    public List<LoanClass> findAll() { return repository.findAll(); }
    public Optional<LoanClass> findById(Long id) { return repository.findById(id); }

    // 1. Выдача физического экземпляра читателю
    public LoanClass create(LoanClass loan) {
        if (loan.getCopyId() != null) {
            CopyClass copy = copyRepository.findById(loan.getCopyId())
                    .orElseThrow(() -> new RuntimeException("Копия не найдена"));

            if ("BORROWED".equalsIgnoreCase(copy.getStatus())) {
                throw new RuntimeException("Эта копия книги уже выдана!");
            }

            copy.setStatus("BORROWED");
            copyRepository.save(copy);
            loan.setCopy(copy);
        } else {
            throw new RuntimeException("Не указан copyId");
        }

        if (loan.getUserId() != null) {
            UserClass user = userRepository.findById(loan.getUserId())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            loan.setUser(user);
        } else {
            throw new RuntimeException("Не указан userId");
        }

        if (loan.getLoanDate() == null || loan.getLoanDate().isEmpty()) {
            loan.setLoanDate(LocalDate.now().toString());
        }

        return repository.save(loan);
    }

    // 2 & 3. Возврат с авто-очередью или продление с проверкой очереди
    public Optional<LoanClass> update(Long id, LoanClass updated) {
        return repository.findById(id).map(loan -> {

            // Возврат книги (если передана returnDate)
            if (updated.getReturnDate() != null && !updated.getReturnDate().isEmpty()) {
                loan.setReturnDate(updated.getReturnDate());

                if (loan.getCopy() != null) {
                    CopyClass copy = loan.getCopy();
                    Long editionId = (copy.getBookEdition() != null) ? copy.getBookEdition().getId() : copy.getBookEditionId();

                    if (editionId != null) {
                        Optional<WaitingRequestClass> waitingRequestOpt = waitingRequestRepository.findAll().stream()
                                .filter(wr -> wr.getBookEditionId() != null &&
                                        wr.getBookEditionId().equals(editionId) &&
                                        "PENDING".equalsIgnoreCase(wr.getStatus()))
                                .findFirst();

                        if (waitingRequestOpt.isPresent()) {
                            WaitingRequestClass waitingRequest = waitingRequestOpt.get();

                            copy.setStatus("BORROWED");
                            copyRepository.save(copy);

                            LoanClass nextLoan = new LoanClass();
                            nextLoan.setCopy(copy);

                            if (waitingRequest.getUserId() != null) {
                                UserClass nextUser = userRepository.findById(waitingRequest.getUserId())
                                        .orElseThrow(() -> new RuntimeException("Пользователь из очереди не найден"));
                                nextLoan.setUser(nextUser);
                            }

                            nextLoan.setLoanDate(LocalDate.now().toString());
                            repository.save(nextLoan);

                            waitingRequestRepository.delete(waitingRequest);
                        } else {
                            copy.setStatus("AVAILABLE");
                            copyRepository.save(copy);
                        }
                    } else {
                        copy.setStatus("AVAILABLE");
                        copyRepository.save(copy);
                    }
                }
            }
            // Продление аренды (если returnDate не передана)
            else {
                if (loan.getCopy() != null) {
                    Long editionId = (loan.getCopy().getBookEdition() != null) ? loan.getCopy().getBookEdition().getId() : loan.getCopy().getBookEditionId();

                    if (editionId != null) {
                        boolean hasWaiting = waitingRequestRepository.findAll().stream()
                                .anyMatch(wr -> wr.getBookEditionId() != null &&
                                        wr.getBookEditionId().equals(editionId) &&
                                        "PENDING".equalsIgnoreCase(wr.getStatus()));

                        if (hasWaiting) {
                            throw new RuntimeException("Невозможно продлить аренду: на эту книгу есть очередь ожидания!");
                        }
                    }
                }

                if (updated.getLoanDate() != null) {
                    loan.setLoanDate(updated.getLoanDate());
                }
            }

            return repository.save(loan);
        });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}