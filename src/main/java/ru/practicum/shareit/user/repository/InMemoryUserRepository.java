package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final AtomicLong lastId = new AtomicLong(0);
    private Map<Long, User> users = new HashMap();

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public User add(User user) {
        Long id = lastId.incrementAndGet();
        user.setId(id);
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public User update(User user) {
        users.replace(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public boolean containsEmail(String email) {
        return users.values().stream().anyMatch(x -> x.getEmail().equals(email));
    }

    @Override
    public boolean userExists(Long id) {
        return users.containsKey(id);
    }
}
