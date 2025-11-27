package ru.practicum.shareit.booking.filterStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FutureBookingStrategy implements BookingFilterStrategy {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getBookingsFromUser(Long userId) {
        return bookingRepository
                .findAllByBookerIdAndStartAfterOrderByStartDesc(
                        userId, LocalDateTime.now());
    }

    @Override
    public List<Booking> getBookingsFromOwner(Long ownerId) {
        return bookingRepository.findFutureByOwner(ownerId, LocalDateTime.now());
    }
}