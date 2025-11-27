package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.filterStrategy.State;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    public static final String USER_Id_HEADER = "X-Sharer-User-Id";

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestBody BookingDtoRequest bookingDto, @RequestHeader(USER_Id_HEADER) Long userId) {
        return bookingService.save(bookingDto, userId);
    }

    @PatchMapping("/{id}")
    public BookingDto changeStatus(@RequestParam("approved") Boolean approved, @PathVariable Long id, @RequestHeader(USER_Id_HEADER) Long userId) {
        return bookingService.changeStatus(approved, id, userId);
    }

    @GetMapping("/{id}")
    public BookingDto get(@PathVariable Long id, @RequestHeader(USER_Id_HEADER) Long userId) {
        return bookingService.getBooking(id, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByUserId(@RequestHeader(USER_Id_HEADER) Long userId, @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingService.getBookingsFromUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwnerId(@RequestHeader(USER_Id_HEADER) Long userId, @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingService.getBookingsFromOwner(userId, state);
    }
}
