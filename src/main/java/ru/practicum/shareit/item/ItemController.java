package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    public static final String USER_Id_HEADER = "X-Sharer-User-Id";

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<ItemRequestDto> getAllFromUser(@RequestHeader(USER_Id_HEADER) Long userId) {
        return itemService.getAllFromUser(userId);
    }

    @GetMapping("/{id}")
    public ItemResponseDto findById(@PathVariable Long id, @RequestHeader(USER_Id_HEADER) Long userId) {
        return itemService.findById(id, userId);
    }

    @PostMapping
    public ItemRequestDto create(@RequestHeader(USER_Id_HEADER) Long userId, @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemService.create(userId, itemRequestDto);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto update(@RequestHeader(USER_Id_HEADER) Long userId, @PathVariable Long id, @RequestBody ItemRequestDto dto) {
        return itemService.update(userId, dto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemRequestDto> search(@RequestParam("text") String text) {
        return itemService.search(text);
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@PathVariable Long id, @RequestHeader(USER_Id_HEADER) Long userId, @RequestBody CommentCreateDto commentCreateDto) {
        return itemService.addComment(id, userId, commentCreateDto);
    }
}
