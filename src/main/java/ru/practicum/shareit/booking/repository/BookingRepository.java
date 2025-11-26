package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> getAllByBookerId(Long bookerId);


    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(
            Long bookerId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(
            Long bookerId, Status status);

    @Query("""
                SELECT b
                FROM Booking b
                WHERE b.booker.id = :userId
                  AND b.item.id = :itemId
                  AND b.end < :now
                  AND b.status = 'APPROVED'
            """)
    List<Booking> findCompletedBookingsByUserAndItem(
            @Param("userId") Long userId,
            @Param("itemId") Long itemId,
            @Param("now") LocalDateTime now);

    @Query("""
                SELECT b
                FROM Booking b
                WHERE b.item.id = :itemId
                  AND b.end < :now
                  AND b.status = 'APPROVED'
                ORDER BY b.end DESC
                LIMIT 1
            """)
    Optional<Booking> getLastBookingForItem(@Param("itemId") Long itemId,
                                            @Param("now") LocalDateTime now);

    @Query("""
                SELECT b
                FROM Booking b
                WHERE b.item.id = :itemId
                  AND b.start > :now
                  AND b.status = 'APPROVED'
                ORDER BY b.start ASC
                LIMIT 1
            """)
    Optional<Booking> getNextBookingForItem(@Param("itemId") Long itemId,
                                            @Param("now") LocalDateTime now);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId
            ORDER BY b.start DESC
            """)
    List<Booking> findAllByItemOwner(@Param("ownerId") Long ownerId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId
            AND b.start <= :now
            AND b.end >= :now
            ORDER BY b.start DESC
            """)
    List<Booking> findCurrentByOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId
            AND b.end < :now
            ORDER BY b.start DESC
            """)
    List<Booking> findPastByOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId
            AND b.start > :now
            ORDER BY b.start DESC
            """)
    List<Booking> findFutureByOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId
            AND b.status = :status
            ORDER BY b.start DESC
            """)
    List<Booking> findByOwnerAndStatus(@Param("ownerId") Long ownerId, @Param("status") Status status);

    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.booker.id = :userId
              AND b.item.id = :itemId
              AND b.end < :now
              AND b.status = 'APPROVED'
            """)
    boolean existsCompletedBooking(Long userId, Long itemId, LocalDateTime now);
}
