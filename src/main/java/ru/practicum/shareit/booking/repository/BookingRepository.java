package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingRepository {
    List<Booking> getAll();

    Booking add(Booking booking);

    Booking findById(Long id);

    void deleteById(Long id);

    Booking update(Booking booking);
}
