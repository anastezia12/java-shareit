package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final AtomicLong lastId = new AtomicLong(0);
    private Map<Long, User> users = new HashMap();
    private Set<String> emails = new HashSet<>();

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public User add(User user) {
        Long id = lastId.incrementAndGet();
        user.setId(id);
        users.put(id, user);
        emails.add(user.getEmail());
        return users.get(id);
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public void deleteById(Long id) {
        emails.remove(users.get(id).getEmail());
        users.remove(id);
    }

    @Override
    public User update(User user) {
        emails.remove(users.get(user.getId()).getEmail());
        users.replace(user.getId(), user);
        emails.add(user.getEmail());
        return users.get(user.getId());
    }

    @Override
    public boolean containsEmail(String email) {
        return emails.contains(email);
    }

    @Override
    public boolean userExists(Long id) {
        return users.containsKey(id);
    }
}
