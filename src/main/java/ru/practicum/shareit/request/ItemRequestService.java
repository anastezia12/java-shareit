package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFullInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestSaveDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemRequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ItemRequestMapper itemRequestMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemRepository itemRepository;

    public ItemRequestDto save(ItemRequestSaveDto itemRequestSaveDto, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("There are no such User with id:" + userId);
        }
        ItemRequest itemRequest = itemRequestMapper.fromSaveDtoToItemRequest(itemRequestSaveDto);
        itemRequest.setRequester(userRepository.getReferenceById(userId));
        itemRequest.setCreated(LocalDateTime.now());
        requestRepository.save(itemRequest);
        return itemRequestMapper.toItemRequestDto(itemRequest);
    }

    public List<ItemRequestFullInfoDto> getMyRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("There are no such User with id:" + userId);
        }
        List<ItemRequest> requests = requestRepository.getByRequesterId(userId);
        List<ItemRequestFullInfoDto> requestFullInfoDto = itemRequestMapper.toItemRequestFullInfoDtoList(requests);
        requestFullInfoDto.forEach(x -> x.setItems(itemMapper.toItemForItemRequestDtoList(itemRepository.getByRequestId(x.getId()))));
        return requestFullInfoDto;
    }

    public List<ItemRequestDto> getAll() {
        return itemRequestMapper.toItemRequestDtoList(requestRepository.findAll());
    }

    public ItemRequestFullInfoDto getById(Long id) {
        if (!requestRepository.existsById(id)) {
            throw new NotFoundException("No such request with id:" + id);
        }
        ItemRequestFullInfoDto requestFullInfoDto = itemRequestMapper.toItemRequestFullInfoDto(requestRepository.getReferenceById(id));
        System.out.println(itemRepository.getByRequestId(id));
        requestFullInfoDto.setItems(itemMapper.toItemForItemRequestDtoList(itemRepository.getByRequestId(id)));

        return requestFullInfoDto;
    }

}
