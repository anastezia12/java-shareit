package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFullInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestSaveDto;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_Id_HEADER = "X-Sharer-User-Id";
    @Autowired
    private ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto save(@Valid @RequestBody ItemRequestSaveDto itemRequestSaveDto, @RequestHeader(USER_Id_HEADER) Long userId) {
        return itemRequestService.save(itemRequestSaveDto, userId);
    }

    @GetMapping
    public List<ItemRequestFullInfoDto> getMyRequests(@RequestHeader(USER_Id_HEADER) Long userId) {
        return itemRequestService.getMyRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getRequests() {
        return itemRequestService.getAll();
    }

    @GetMapping("/{requestId}")
    public ItemRequestFullInfoDto getById(@PathVariable Long requestId) {
        return itemRequestService.getById(requestId);
    }
}
