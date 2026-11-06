package org.example.API.waitingrequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRequestRepository extends JpaRepository<WaitingRequestClass, Long> {
}