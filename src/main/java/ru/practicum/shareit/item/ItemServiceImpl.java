package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private RequestRepository requestRepository;

    @Override
    public List<ItemResponseDto> getAllFromUser(Long id) {
        List<ItemResponseDto> items =
                itemMapper.toItemResponseDtoList(itemRepository.findByOwnerId(id));
        for (ItemResponseDto dto : items) {
            addBookingsToItem(dto);
            dto.setComments(
                    commentMapper.toDtoList(
                            commentRepository.getByItemId(dto.getId())
                    )
            );
        }
        return items;
    }


    public ItemResponseDto addBookingsToItem(ItemResponseDto dto) {

        dto.setLastBooking(bookingRepository.getLastBookingForItem(dto.getId(), LocalDateTime.now()).map(bookingMapper::toDto).orElse(null));

        dto.setNextBooking(bookingRepository.getNextBookingForItem(dto.getId(), LocalDateTime.now()).map(bookingMapper::toDto).orElse(null));
        return dto;
    }

    @Override
    public ItemResponseDto findById(Long id, Long userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("No such item with id: " + id));

        ItemResponseDto dto = itemMapper.toItemResponseDto(item);

        boolean isOwner = item.getOwner().getId().equals(userId);

        if (isOwner) {
            dto.setLastBooking(bookingRepository.getLastBookingForItem(id, LocalDateTime.now()).map(bookingMapper::toDto).orElse(null));
            dto.setNextBooking(bookingRepository.getNextBookingForItem(id, LocalDateTime.now()).map(bookingMapper::toDto).orElse(null));
        } else {
            dto.setLastBooking(null);
            dto.setNextBooking(null);
        }
        dto.setComments(commentMapper.toDtoList(commentRepository.getByItemId(dto.getId())));

        return dto;
    }

    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("No such User with id: " + userId);
        }
        Item item = itemMapper.fromDto(itemRequestDto);
        item.setOwner(userRepository.getReferenceById(userId));
        if (itemRequestDto.getRequestId() != null) {
            item.setRequest(requestRepository.getReferenceById(itemRequestDto.getRequestId()));
        }
        return itemMapper.toItemDto(itemRepository.save(item));
    }


    @Override
    public ItemResponseDto update(Long userId, ItemRequestDto itemRequestDto, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("No such item with id: " + itemId));

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("No such User with id: " + userId);
        }

        itemMapper.updateItemFromDto(itemRequestDto, item);

        return addBookingsToItem(itemMapper.toItemResponseDto(itemRepository.save(item)));
    }


    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemRequestDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemMapper.toItemDtoList(itemRepository.search(text));
    }


    @Override
    public CommentDto addComment(Long itemId, Long userId, CommentCreateDto text) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("No such user with id: " + userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("No such item with id: " + itemId));

        if (!bookingRepository.existsCompletedBooking(userId, itemId, LocalDateTime.now())) {
            throw new IllegalArgumentException("User must have completed a booking for this item before commenting");
        }
        Comment comment = new Comment();
        comment.setText(text.getText());
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(comment));
    }
}
