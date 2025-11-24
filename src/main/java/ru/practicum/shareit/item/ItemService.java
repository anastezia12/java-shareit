package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {
    List<ItemRequestDto> getAllFromUser(Long id);

    ItemResponseDto findById(Long id, Long userId);

    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);

    ItemResponseDto update(Long userId, ItemRequestDto itemRequestDto, Long itemId);

    void delete(Long id);

    List<ItemRequestDto> search(String text);

    CommentDto addComment(Long itemId, Long userId, CommentCreateDto text);
}
