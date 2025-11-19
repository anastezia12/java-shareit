package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    public static final String USER_Id_HEADER = "X-Sharer-User-Id";

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllFromUser(@RequestHeader(USER_Id_HEADER) Long userId) {
        return itemService.getAllFromUser(userId);
    }

    @GetMapping("/{id}")
    public ItemDto findById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(USER_Id_HEADER) Long userId,
                          @RequestBody @Valid ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(USER_Id_HEADER) Long userId,
                          @PathVariable Long id,
                          @RequestBody ItemDto dto) {
        return itemService.update(userId, dto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        return itemService.search(text);
    }
}
