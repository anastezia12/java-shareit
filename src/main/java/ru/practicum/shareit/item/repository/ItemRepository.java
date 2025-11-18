package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item add(Item item);

    Item getById(Long id);

    List<Item> getAll();

    void deleteById(Long id);

    Item update(Item item);

    boolean containsItem(Long id);

    List<Item> getAllFromUser(Long id);

    List<Item> search(String text);
}
