package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    public BookingDto save(BookingDtoRequest bookingDto, Long userId) {
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundException("No such item with id:" + bookingDto.getItemId());
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("No such user with id:" + userId);
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new IllegalArgumentException("Start time should be before end time");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start time should be before end time");
        }
        if (!itemRepository.getReferenceById(bookingDto.getItemId()).getAvailable()) {
            throw new NotAvailableException("Item is not available");
        }
        Booking booking = bookingMapper.fromDto(bookingDto);
        booking.setStatus(Status.WAITING);
        booking.setBooker(userRepository.getReferenceById(userId));
        BookingDto answer = bookingMapper.toDto(bookingRepository.save(booking));
        answer.getItem().setName(itemRepository.getById(bookingDto.getItemId()).getName());
        return answer;
    }

    public BookingDto changeStatus(Boolean approved, Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such booking with id: " + id));

        Long ownerId = booking.getItem().getOwner().getId();

        if (!Objects.equals(ownerId, userId)) {
            throw new IllegalArgumentException("Only the owner of the item can change the booking status");
        }
        if (booking.getStatus() == Status.APPROVED && approved) {
            throw new IllegalStateException("Booking is already approved");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingMapper.toDto(bookingRepository.save(booking));
    }


    public BookingDto getBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);

        Long bookerId = booking.getBooker().getId();
        Long ownerId = booking.getItem().getOwner().getId();

        if (!Objects.equals(bookerId, userId) && !Objects.equals(ownerId, userId)) {
            throw new IllegalArgumentException("Only the owner or the booker can see booking info");
        }

        return bookingMapper.toDto(booking);
    }

    public List<BookingDto> getBookingsFromUser(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("No such user with id:" + userId);
        }

        List<Booking> bookings;

        LocalDateTime now = LocalDateTime.now();

        switch (state.toUpperCase()) {
            case "CURRENT":
                bookings = bookingRepository
                        .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                                userId, now, now);
                break;
            case "PAST":
                bookings = bookingRepository
                        .findAllByBookerIdAndEndBeforeOrderByStartDesc(
                                userId, now);
                break;
            case "FUTURE":
                bookings = bookingRepository
                        .findAllByBookerIdAndStartAfterOrderByStartDesc(
                                userId, now);
                break;
            case "WAITING":
                bookings = bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(
                                userId, Status.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(
                                userId, Status.REJECTED);
                break;
            case "ALL":
            default:
                bookings = bookingRepository
                        .findAllByBookerIdOrderByStartDesc(userId);
                break;
        }

        return bookingMapper.toDtoList(bookings);
    }

    public List<BookingDto> getBookingsFromOwner(Long ownerId, String state) {
        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state.toUpperCase()) {
            case "CURRENT":
                bookings = bookingRepository.findCurrentByOwner(ownerId, now);
                break;
            case "PAST":
                bookings = bookingRepository.findPastByOwner(ownerId, now);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureByOwner(ownerId, now);
                break;
            case "WAITING":
                bookings = bookingRepository.findByOwnerAndStatus(ownerId, Status.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByOwnerAndStatus(ownerId, Status.REJECTED);
                break;
            case "ALL":
            default:
                bookings = bookingRepository.findAllByItemOwner(ownerId);
                break;
        }

        return bookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }
}
