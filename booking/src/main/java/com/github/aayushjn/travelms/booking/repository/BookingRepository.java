package com.github.aayushjn.travelms.booking.repository;

import com.github.aayushjn.travelms.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    @Query("SELECT b FROM Booking b WHERE b.bookingId = ?1")
    List<Booking> getAllBookingsForUserId(int userId);
}
