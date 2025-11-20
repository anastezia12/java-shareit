package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong lastId = new AtomicLong(0);

    @Override
    public Item add(Item item) {
        Long id = lastId.incrementAndGet();
        items.put(id, item);
        item.setId(id);
        return items.get(id);
    }

    @Override
    public Item getById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAll() {
        return items.values().stream().toList();
    }

    @Override
    public void deleteById(Long id) {
        items.remove(id);
    }

    @Override
    public Item update(Item item) {
        items.replace(item.getId(), item);
        return item;
    }

    @Override
    public boolean containsItem(Long id) {
        return items.containsKey(id);
    }

    @Override
    public List<Item> getAllFromUser(Long id) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().equals(id))
                .toList();
    }

    @Override
    public List<Item> search(String text) {
        String lowerText = text.toLowerCase();

        return items.values()
                .stream()
                .filter(item -> item.getAvailable()
                        && ((item.getName() != null && item.getName().toLowerCase().contains(lowerText))
                        || (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerText))))
                .toList();
    }

}
