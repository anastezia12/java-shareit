package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<ItemDto> getAllFromUser(Long id) {
        return itemMapper.toItemDtoList(itemRepository.getAllFromUser(id));
    }

    @Override
    public ItemDto findById(Long id) {
        return itemMapper.toItemDto(itemRepository.getById(id));
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        if (!userRepository.userExists(userId)) {
            throw new UserNotFoundException("No such User with id: " + userId);
        }
        Item item = itemMapper.fromDto(itemDto);
        item.setOwner(userId);
        return itemMapper.toItemDto(itemRepository.add(item));
    }


    @Override
    public ItemDto update(Long userId, ItemDto itemDto, Long itemId) {
        if (!itemRepository.containsItem(itemId)) {
            throw new IllegalArgumentException("There are no such item with id: " + itemId);
        }
        if (!userRepository.userExists(userId)) {
            throw new UserNotFoundException("No such User with id: " + userId);
        }

        Item item = itemRepository.getById(itemId);

        itemMapper.updateItemFromDto(itemDto, item);

        return itemMapper.toItemDto(itemRepository.update(item));
    }


    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemMapper.toItemDtoList(itemRepository.search(text));
    }
}
