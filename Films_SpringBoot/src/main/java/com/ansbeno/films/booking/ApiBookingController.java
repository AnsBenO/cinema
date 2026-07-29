package com.ansbeno.films.booking;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookings")
public class ApiBookingController {

      private final BookingService bookingService;

      @PostMapping
      public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody CreateBookingPayload payload,
                  Authentication authentication) {
            BookingDto booking = bookingService.createBooking(authentication.getName(), payload);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
      }

      @GetMapping("/me")
      public ResponseEntity<List<BookingDto>> getMyBookings(Authentication authentication) {
            return ResponseEntity.ok(bookingService.getMyBookings(authentication.getName()));
      }

      @GetMapping("/availability/{screeningId}")
      public ResponseEntity<ScreeningAvailabilityDto> getAvailability(@PathVariable Long screeningId) {
            return ResponseEntity.ok(bookingService.getAvailability(screeningId));
      }

      @DeleteMapping("/{bookingId}")
      public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long bookingId, Authentication authentication) {
            return ResponseEntity.ok(bookingService.cancelBooking(authentication.getName(), bookingId));
      }
}
