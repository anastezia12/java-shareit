package ru.practicum.shareit.request.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryRequestRepository implements RequestRepository {
    private final AtomicLong lastId = new AtomicLong(0);
    private Map<Long, ItemRequest> requests = new HashMap();

    @Override
    public ItemRequest add(ItemRequest request) {
        Long id = lastId.incrementAndGet();
        request.setId(id);
        requests.put(id, request);
        return requests.get(id);
    }

    @Override
    public ItemRequest getById(Long id) {
        return requests.get(id);
    }

    @Override
    public List<ItemRequest> getAll() {
        return requests.values().stream().toList();
    }

    @Override
    public void deleteById(Long id) {
        requests.remove(id);
    }

    @Override
    public ItemRequest update(ItemRequest request) {
        requests.replace(request.getId(), request);
        return requests.get(request.getId());
    }
}
