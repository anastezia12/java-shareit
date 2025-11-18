package ru.practicum.shareit.booking.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryBookingRepository implements BookingRepository {
    private final AtomicLong lastId = new AtomicLong(0);
    private Map<Long, Booking> bookings = new HashMap();

    @Override
    public List<Booking> getAll() {
        return bookings.values().stream().toList();
    }

    @Override
    public Booking add(Booking booking) {
        Long id = lastId.incrementAndGet();
        booking.setId(id);
        bookings.put(id, booking);
        return bookings.get(id);
    }

    @Override
    public Booking findById(Long id) {
        return bookings.get(id);
    }

    @Override
    public void deleteById(Long id) {
        bookings.remove(id);
    }

    @Override
    public Booking update(Booking booking) {
        bookings.replace(booking.getId(), booking);
        return bookings.get(booking.getId());
    }
}
