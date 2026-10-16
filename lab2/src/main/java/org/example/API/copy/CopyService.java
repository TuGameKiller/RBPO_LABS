package org.example.API.copy;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CopyService {
    private final CopyRepository repository;

    public CopyService(CopyRepository repository) {
        this.repository = repository;
    }

    public List<CopyClass> findAll() {
        return repository.findAll();
    }

    public Optional<CopyClass> findById(Long id) {
        return repository.findById(id);
    }

    public CopyClass create(CopyClass copy) {
        return repository.save(copy);
    }

    public Optional<CopyClass> update(Long id, CopyClass updatedCopy) {
        return repository.findById(id).map(copy -> {
            if (updatedCopy.getBookEditionId() != null) copy.setBookEditionId(updatedCopy.getBookEditionId());
            if (updatedCopy.getInventoryNumber() != null) copy.setInventoryNumber(updatedCopy.getInventoryNumber());
            if (updatedCopy.getStatus() != null) copy.setStatus(updatedCopy.getStatus());
            return repository.save(copy);
        });
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}