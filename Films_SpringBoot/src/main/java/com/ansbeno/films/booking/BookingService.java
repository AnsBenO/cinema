package com.ansbeno.films.booking;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ansbeno.films.screening.Screening;
import com.ansbeno.films.screening.ScreeningRepository;
import com.ansbeno.films.shared.exception.BookingAccessDeniedException;
import com.ansbeno.films.shared.exception.BookingCapacityExceededException;
import com.ansbeno.films.shared.exception.BookingOperationNotAllowedException;
import com.ansbeno.films.shared.exception.ResourceNotFoundException;
import com.ansbeno.films.user.UserEntity;
import com.ansbeno.films.user.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

      private static final ZoneId APP_ZONE = ZoneId.systemDefault();

      private final BookingRepository bookingRepository;
      private final ScreeningRepository screeningRepository;
      private final UserRepository userRepository;
      private final BookingMapper bookingMapper;

      @Transactional
      public BookingDto createBooking(@NonNull String email, @NonNull CreateBookingPayload payload) {
            UserEntity customer = userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Screening screening = screeningRepository.findWithLockById(payload.screeningId())
                        .orElseThrow(() -> new ResourceNotFoundException("Screening not found"));

            if (!screening.getStartTime().isAfter(LocalDateTime.now(APP_ZONE))) {
                  throw new BookingOperationNotAllowedException("Cannot book a screening that has already started");
            }

            int reservedSeats = bookingRepository.getBookedSeatsForScreening(screening.getId());
            int remainingSeats = screening.getHall().getCapacity() - reservedSeats;

            if (payload.quantity() > remainingSeats) {
                  throw new BookingCapacityExceededException(
                              "Not enough seats available. Remaining seats: " + remainingSeats);
            }

            Booking booking = Booking.builder()
                        .customer(customer)
                        .screening(screening)
                        .quantity(payload.quantity())
                        .status(BookingStatus.BOOKED)
                        .build();

            Booking savedBooking = bookingRepository.save(Objects.requireNonNull(booking));
            return bookingMapper.toDto(savedBooking);
      }

      @Transactional(readOnly = true)
      public List<BookingDto> getMyBookings(@NonNull String email) {
            return bookingRepository.findByCustomer_EmailOrderByCreatedAtDesc(email)
                        .stream()
                        .map(bookingMapper::toDto)
                        .toList();
      }

      @Transactional(readOnly = true)
      public ScreeningAvailabilityDto getAvailability(@NonNull Long screeningId) {
            Screening screening = screeningRepository.findById(screeningId)
                        .orElseThrow(() -> new ResourceNotFoundException("Screening not found"));

            int reservedSeats = bookingRepository.getBookedSeatsForScreening(screeningId);
            int hallCapacity = screening.getHall().getCapacity();
            int remainingSeats = Math.max(0, hallCapacity - reservedSeats);

            return ScreeningAvailabilityDto.builder()
                        .screeningId(screeningId)
                        .hallCapacity(hallCapacity)
                        .reservedSeats(reservedSeats)
                        .remainingSeats(remainingSeats)
                        .build();
      }

      @Transactional
      public BookingDto cancelBooking(@NonNull String email, @NonNull Long bookingId) {
            Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

            if (!booking.getCustomer().getEmail().equals(email)) {
                  throw new BookingAccessDeniedException("You are not allowed to cancel this booking");
            }

            if (booking.getStatus() == BookingStatus.CANCELED) {
                  throw new BookingOperationNotAllowedException("Booking is already canceled");
            }

            if (!booking.getScreening().getStartTime().isAfter(LocalDateTime.now(APP_ZONE))) {
                  throw new BookingOperationNotAllowedException("Cannot cancel booking after screening start time");
            }

            booking.setStatus(BookingStatus.CANCELED);
            return bookingMapper.toDto(bookingRepository.save(booking));
      }
}
