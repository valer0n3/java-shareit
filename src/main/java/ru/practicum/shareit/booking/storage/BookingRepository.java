package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

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

    @Query("SELECT i FROM Booking i WHERE i.item.owner.id = ?1 ORDER BY i.start DESC")
    List<Booking> getAllBookingsOfItemsOwner(int ownerId);

    @Query("SELECT i FROM Booking i WHERE i.item.owner.id = ?1 " +
            "AND (i.start < CURRENT_TIMESTAMP) " +
            "AND (i.end > current_timestamp ) ORDER BY i.start DESC")
    List<Booking> getCurrentBookingsOfItemsOwner(int ownerId);

    @Query("SELECT i FROM Booking i WHERE i.item.owner.id = ?1 " +
            "AND (i.end < current_timestamp) ORDER BY i.start DESC")
    List<Booking> getPastBookingsOfItemsOwner(int ownerId);

    @Query("SELECT i FROM Booking i WHERE i.item.owner.id = ?1 " +
            "AND (i.start > CURRENT_TIMESTAMP) ORDER BY i.start DESC")
    List<Booking> getFutureBookingsOfItemsOwner(int ownerId);

    @Query("SELECT i FROM Booking i WHERE i.item.owner.id = ?1 " +
            "AND (i.status = ?2) ORDER BY i.start DESC")
    List<Booking> getBookingsOfItemsOwner(int ownerId, BookingStatusEnum bookingStatus);

    @Query(value = "SELECT * FROM bookings  WHERE item_id = ?1 " +
            "AND (end_date < CURRENT_TIMESTAMP) ORDER BY end_date DESC LIMIT 1", nativeQuery = true)
    Booking searchLatestBooking(int itemId);

    @Query(value = "SELECT * FROM bookings  WHERE item_id = ?1 " +
            "AND (start_date > CURRENT_TIMESTAMP) ORDER BY start_date ASC LIMIT 1", nativeQuery = true)
    Booking searchNearestBooking(int itemId);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = ?1 AND item_id = ?2 " +
            "AND start_date < CURRENT_TIMESTAMP " +
            "AND (status = 'APPROVED')", nativeQuery = true)
    Optional<Booking> checkIfUserBookedItem(int userId, int itemId);

    @Query("SELECT i FROM Booking i WHERE i.item.id = ?1 ORDER BY i.id")
    List<Booking> getBookingForItem(int itemId);

    @Query("SELECT i FROM Booking i WHERE i.item.owner.id = ?1 ORDER BY i.id")
    List<Booking> getBookingForOwner(int ownerId);
}

