package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(value = "SELECT * FROM bookings  WHERE booker_id = ?1 " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> getAllBookingsOfCurrentUser(int userID);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = ?1 " +
            "AND (start_date < CURRENT_TIMESTAMP) AND (end_date > CURRENT_TIMESTAMP) " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> getCurrentBookingsOfCurrentUser(int userID);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = ?1 " +
            "AND (end_date < CURRENT_TIMESTAMP) " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> getPastBookingsOfCurrentUser(int userID);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = ?1 " +
            "AND (start_date > CURRENT_TIMESTAMP) " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> getFutureBookingsOfCurrentUser(int userID);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = ?1 " +
            "AND (status = ?2) ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> getBookingsOfCurrentUser(int userID, String bookingStatus);
}
