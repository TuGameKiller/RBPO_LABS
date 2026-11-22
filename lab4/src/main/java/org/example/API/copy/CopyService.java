package org.example.API.copy;

import org.example.API.EntityHelper;
import org.example.API.bookedition.BookEditionClass;
import org.example.API.bookedition.BookEditionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public List<CopyClass> findAll() {
        return repository.findAll();
    }

    public Optional<CopyClass> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public CopyClass create(CopyClass copy) {
        BookEditionClass edition = EntityHelper.findOrThrow(
                bookEditionRepository,
                copy.getBookEditionId(),
                "Не указан или не найден bookEditionId"
        );
        copy.setBookEdition(edition);

        String invNumber = EntityHelper.requireNonBlank(
                copy.getInventoryNumber(),
                "Не указан инвентарный номер (inventoryNumber)"
        );
        if (repository.existsByInventoryNumber(invNumber)) {
            throw new IllegalArgumentException("Экземпляр с инвентарным номером '" + invNumber + "' уже существует");
        }

        if (copy.getStatus() == null || copy.getStatus().isBlank()) {
            copy.setStatus("AVAILABLE");
        }
        return repository.save(copy);
    }

    @Transactional
    public Optional<CopyClass> update(Long id, CopyClass updated) {
        return repository.findById(id).map(copy -> {
            if (updated.getBookEditionId() != null) {
                BookEditionClass edition = EntityHelper.findOrThrow(
                        bookEditionRepository,
                        updated.getBookEditionId(),
                        "Книжное издание с ID " + updated.getBookEditionId() + " не найдено"
                );
                copy.setBookEdition(edition);
            }
            EntityHelper.updateIfPresent(
                    updated.getInventoryNumber(),
                    "Инвентарный номер не может быть пустым",
                    invNumber -> {
                        if (repository.existsByInventoryNumberAndIdNot(invNumber, id)) {
                            throw new IllegalArgumentException("Экземпляр с инвентарным номером '" + invNumber + "' уже существует");
                        }
                        copy.setInventoryNumber(invNumber);
                    }
            );
            EntityHelper.updateIfPresent(
                    updated.getStatus(),
                    "Статус экземпляра не может быть пустым",
                    copy::setStatus
            );
            return repository.save(copy);
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

    // Бизнес-операция: списание экземпляра из фонда
    @Transactional
    public CopyClass writeOffCopy(Long id) {
        CopyClass copy = EntityHelper.findOrThrow(
                repository,
                id,
                "Экземпляр книги с ID " + id + " не найден"
        );

        if ("BORROWED".equalsIgnoreCase(copy.getStatus())) {
            throw new IllegalStateException("Невозможно списать экземпляр: книга находится на руках у читателя");
        }
        if ("WRITTEN_OFF".equalsIgnoreCase(copy.getStatus())) {
            throw new IllegalStateException("Экземпляр уже списан");
        }

        copy.setStatus("WRITTEN_OFF");
        return repository.save(copy);
    }
}