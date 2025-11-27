package ru.practicum.shareit.booking.filterStrategy;

import ru.practicum.shareit.booking.Booking;

import java.util.List;


public interface BookingFilterStrategy {

    List<Booking> getBookingsFromUser(Long userId);

    List<Booking> getBookingsFromOwner(Long ownerId);
}
