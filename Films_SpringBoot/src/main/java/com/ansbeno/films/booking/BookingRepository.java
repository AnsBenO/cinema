package com.ansbeno.films.booking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

      @Query("""
                  SELECT COALESCE(SUM(b.quantity), 0)
                  FROM Booking b
                  WHERE b.screening.id = :screeningId
                    AND b.status = com.ansbeno.films.booking.BookingStatus.BOOKED
                  """)
      int getBookedSeatsForScreening(@Param("screeningId") Long screeningId);

      List<Booking> findByCustomer_EmailOrderByCreatedAtDesc(String email);
}
