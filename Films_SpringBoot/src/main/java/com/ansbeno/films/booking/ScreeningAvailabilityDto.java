package com.ansbeno.films.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ScreeningAvailabilityDto {
      private Long screeningId;
      private int hallCapacity;
      private int reservedSeats;
      private int remainingSeats;
}
