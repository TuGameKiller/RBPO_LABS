package org.example.API.copy;

import org.example.API.bookedition.BookEditionClass;
import org.example.API.bookedition.BookEditionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CopyService {
    private final CopyRepository repository;
    private final BookEditionRepository bookEditionRepository;

    public CopyService(CopyRepository repository, BookEditionRepository bookEditionRepository) {
        this.repository = repository;
        this.bookEditionRepository = bookEditionRepository;
    }

    public List<CopyClass> findAll() { return repository.findAll(); }
    public Optional<CopyClass> findById(Long id) { return repository.findById(id); }

    public CopyClass create(CopyClass copy) {
        if (copy.getBookEditionId() != null) {
            BookEditionClass edition = bookEditionRepository.findById(copy.getBookEditionId())
                    .orElseThrow(() -> new RuntimeException("Книжное издание не найдено"));
            copy.setBookEdition(edition);
        } else {
            throw new RuntimeException("Не указан bookEditionId");
        }

        if (copy.getStatus() == null || copy.getStatus().isEmpty()) {
            copy.setStatus("AVAILABLE");
        }
        return repository.save(copy);
    }

    public Optional<CopyClass> update(Long id, CopyClass updated) {
        return repository.findById(id).map(copy -> {
            if (updated.getStatus() != null) {
                copy.setStatus(updated.getStatus());
            }
            if (updated.getInventoryNumber() != null) {
                copy.setInventoryNumber(updated.getInventoryNumber());
            }
            return repository.save(copy);
        });
    }

    // 5. Списание / вывод экземпляра из фонда
    public boolean delete(Long id) {
        CopyClass copy = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Экземпляр книги не найден"));

        if ("BORROWED".equalsIgnoreCase(copy.getStatus())) {
            throw new RuntimeException("Невозможно списать экземпляр: книга находится на руках у читателя!");
        }

        repository.deleteById(id);
        return true;
    }
}