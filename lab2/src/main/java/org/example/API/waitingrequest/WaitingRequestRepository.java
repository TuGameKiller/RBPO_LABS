package org.example.API.waitingrequest;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class WaitingRequestRepository {
    private final List<WaitingRequestClass> storage = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<WaitingRequestClass> findAll() {
        return storage;
    }

    public Optional<WaitingRequestClass> findById(Long id) {
        return storage.stream().filter(w -> w.getId().equals(id)).findFirst();
    }

    public WaitingRequestClass save(WaitingRequestClass waitreq) {
        if (waitreq.getId() == null) {
            waitreq.setId(idCounter.getAndIncrement());
            storage.add(waitreq);
        } else {
            delete(waitreq.getId());
            storage.add(waitreq);
        }
        return waitreq;
    }

    public boolean delete(Long id) {
        return storage.removeIf(w -> w.getId().equals(id));
    }
}