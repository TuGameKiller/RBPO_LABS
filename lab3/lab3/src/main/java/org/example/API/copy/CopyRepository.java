package org.example.API.copy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopyRepository extends JpaRepository<CopyClass, Long> {
    boolean existsByInventoryNumber(String inventoryNumber);
    boolean existsByInventoryNumberAndIdNot(String inventoryNumber, Long id);
}