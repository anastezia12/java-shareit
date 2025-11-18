package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface RequestRepository {
    ItemRequest add(ItemRequest request);

    ItemRequest getById(Long id);

    List<ItemRequest> getAll();

    void deleteById(Long id);

    ItemRequest update(ItemRequest request);
}
