package ru.practicum.shareit.booking.filterStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

@Component
public class AllBookingStrategy implements BookingFilterStrategy {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getBookingsFromUser(Long userId) {
        return bookingRepository
                .findAllByBookerIdOrderByStartDesc(userId);
    }

    @Override
    public List<Booking> getBookingsFromOwner(Long ownerId) {
        return bookingRepository.findAllByItemOwner(ownerId);
    }
}
