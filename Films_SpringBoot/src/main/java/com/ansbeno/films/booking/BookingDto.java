package com.ansbeno.films.booking;

import java.time.LocalDateTime;

import com.ansbeno.films.shared.base.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class BookingDto extends BaseDto {
      private Long id;
      private Long screeningId;
      private String filmTitle;
      private LocalDateTime screeningStartTime;
      private int hallNumber;
      private int quantity;
      private BookingStatus status;
      private LocalDateTime bookedAt;
}
