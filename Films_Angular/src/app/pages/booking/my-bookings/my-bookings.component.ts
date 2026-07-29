import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { take } from 'rxjs';
import { Booking } from '../../../models/booking.model';
import { AuthService } from '../../../services/auth.service';
import { BookingService } from '../../../services/booking.service';
import {
  NotificationStore,
  NotificationType,
} from '../../../store/notification.store';

@Component({
  selector: 'app-my-bookings',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './my-bookings.component.html',
  styleUrl: './my-bookings.component.css',
})
export class MyBookingsComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly bookingService = inject(BookingService);
  private readonly notificationStore = inject(NotificationStore);
  private readonly router = inject(Router);

  readonly bookings = signal<Booking[]>([]);
  readonly isLoading = signal(true);
  readonly isCancelling = signal<Record<number, boolean>>({});

  ngOnInit(): void {
    this.authService.user$.pipe(take(1)).subscribe((user) => {
      if (!user) {
        this.notificationStore.notify(
          'Please log in to access your bookings',
          NotificationType.INFO,
        );
        this.router.navigateByUrl('/login');
        return;
      }

      this.loadBookings();
    });
  }

  loadBookings(): void {
    this.isLoading.set(true);
    this.bookingService.getMyBookings().subscribe({
      next: (bookings) => {
        this.bookings.set(bookings);
        this.isLoading.set(false);
      },
      error: () => {
        this.notificationStore.notify(
          'Could not load your bookings',
          NotificationType.ERROR,
        );
        this.isLoading.set(false);
      },
    });
  }

  cancelBooking(bookingId: number): void {
    this.isCancelling.update((state) => ({ ...state, [bookingId]: true }));
    this.bookingService.cancelBooking(bookingId).subscribe({
      next: (updatedBooking) => {
        this.bookings.update((bookings) =>
          bookings.map((booking) =>
            booking.id === updatedBooking.id ? updatedBooking : booking,
          ),
        );
        this.notificationStore.notify(
          'Booking canceled successfully',
          NotificationType.SUCCESS,
        );
        this.isCancelling.update((state) => ({ ...state, [bookingId]: false }));
      },
      error: (error) => {
        const message =
          error?.error?.error ||
          error?.error?.message ||
          'Could not cancel booking';
        this.notificationStore.notify(message, NotificationType.ERROR);
        this.isCancelling.update((state) => ({ ...state, [bookingId]: false }));
      },
    });
  }

  canCancel(booking: Booking): boolean {
    return (
      booking.status === 'BOOKED' &&
      new Date(booking.screeningStartTime).getTime() > Date.now()
    );
  }
}
