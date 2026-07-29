package com.ansbeno.films.booking;

import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

      public BookingDto toDto(Booking booking) {
            return BookingDto.builder()
                        .id(booking.getId())
                        .screeningId(booking.getScreening().getId())
                        .filmTitle(booking.getScreening().getFilm().getTitle())
                        .screeningStartTime(booking.getScreening().getStartTime())
                        .hallNumber(booking.getScreening().getHall().getNumber())
                        .quantity(booking.getQuantity())
                        .status(booking.getStatus())
                        .bookedAt(booking.getBookedAt())
                        .build();
      }
}
