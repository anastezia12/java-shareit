package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User add(User user);

    User findById(Long id);

    void deleteById(Long id);

    User update(User user);

    boolean containsEmail(String email);

    boolean userExists(Long id);
}
