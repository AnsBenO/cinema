import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  inject,
  signal,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faMinus, faPlus, faXmark } from '@fortawesome/free-solid-svg-icons';
import { Dialog } from 'primeng/dialog';
import { BookingService } from '../../services/booking.service';
import { AuthService } from '../../services/auth.service';
import {
  NotificationStore,
  NotificationType,
} from '../../store/notification.store';
import { Screening } from '../../models/screening.model';
import { Router } from '@angular/router';
import { ScreeningAvailability } from '../../models/booking.model';
import { take } from 'rxjs';

type BookingStep = 'tickets' | 'payment';

export interface BookingCompletedEvent {
  screeningId: number;
  quantity: number;
}

@Component({
    selector: 'app-book-ticket',
    imports: [CommonModule, FormsModule, Dialog, FontAwesomeModule],
    templateUrl: './book-ticket.component.html',
    styleUrl: './book-ticket.component.css'
})
export class BookTicketComponent implements OnChanges {
  private readonly bookingService = inject(BookingService);
  private readonly authService = inject(AuthService);
  private readonly notificationStore = inject(NotificationStore);
  private readonly router = inject(Router);

  @Input({ required: true }) visible!: boolean;
  @Output() visibleChange = new EventEmitter<boolean>();

  @Input({ required: true }) screening: Screening | null = null;

  @Output() bookingCompleted = new EventEmitter<BookingCompletedEvent>();

  readonly closeIcon = faXmark;
  readonly plusIcon = faPlus;
  readonly minusIcon = faMinus;

  readonly quantity = signal(1);
  readonly isSubmitting = signal(false);
  readonly isLoadingAvailability = signal(false);
  readonly availability = signal<ScreeningAvailability | null>(null);
  readonly currentStep = signal<BookingStep>('tickets');

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['screening']) {
      this.resetState();
      if (this.visible && this.screening) {
        this.loadAvailability();
      }
    }

    if (changes['visible'] && this.visible && this.screening) {
      this.loadAvailability();
    }
  }

  get remainingSeats(): number {
    const availability = this.availability();
    if (availability) {
      return availability.remainingSeats;
    }
    return this.screening?.hall.capacity ?? 0;
  }

  get isSoldOut(): boolean {
    return this.remainingSeats <= 0;
  }

  updateQuantity(rawValue: number): void {
    const parsed = Number.isFinite(rawValue) ? Math.floor(rawValue) : 1;
    const minQuantity = 1;
    const maxQuantity = Math.max(minQuantity, this.remainingSeats);
    this.quantity.set(Math.min(Math.max(parsed, minQuantity), maxQuantity));
  }

  decreaseQuantity(): void {
    this.updateQuantity(this.quantity() - 1);
  }

  increaseQuantity(): void {
    this.updateQuantity(this.quantity() + 1);
  }

  goToPaymentStep(): void {
    if (this.isSoldOut) {
      this.notificationStore.notify(
        'This screening is sold out',
        NotificationType.INFO,
      );
      return;
    }

    this.updateQuantity(this.quantity());
    this.currentStep.set('payment');
  }

  goBackToTickets(): void {
    this.currentStep.set('tickets');
  }

  confirmBooking(): void {
    const selectedScreening = this.screening;
    if (!selectedScreening) {
      return;
    }

    this.authService.user$.pipe(take(1)).subscribe((currentUser) => {
      if (!currentUser) {
        this.notificationStore.notify(
          'Please log in to continue with payment',
          NotificationType.INFO,
        );
        this.closeDialog();
        this.router.navigate(['/login'], {
          queryParams: { returnUrl: '/home' },
        });
        return;
      }

      if (this.isSoldOut) {
        this.notificationStore.notify(
          'No seats left for this screening',
          NotificationType.ERROR,
        );
        return;
      }

      this.isSubmitting.set(true);
      const quantity = this.quantity();
      const screeningId = selectedScreening.id;

      this.bookingService
        .createBooking({
          screeningId,
          quantity,
        })
        .subscribe({
          next: () => {
            this.notificationStore.notify(
              'Booking created successfully',
              NotificationType.SUCCESS,
            );
            this.bookingCompleted.emit({
              screeningId,
              quantity,
            });
            this.isSubmitting.set(false);
            this.closeDialog();
          },
          error: (error) => {
            const message =
              error?.error?.error ||
              error?.error?.message ||
              'Could not create booking';
            this.notificationStore.notify(message, NotificationType.ERROR);
            this.isSubmitting.set(false);
            this.loadAvailability();
          },
        });
    });
  }

  closeDialog(): void {
    this.visible = false;
    this.visibleChange.emit(this.visible);
    this.resetState();
  }

  private loadAvailability(): void {
    if (!this.screening) {
      return;
    }

    this.isLoadingAvailability.set(true);
    this.bookingService.getAvailability(this.screening.id).subscribe({
      next: (availability) => {
        this.availability.set(availability);
        this.updateQuantity(this.quantity());
        this.isLoadingAvailability.set(false);
      },
      error: () => {
        this.availability.set({
          screeningId: this.screening!.id,
          hallCapacity: this.screening!.hall.capacity,
          reservedSeats: 0,
          remainingSeats: this.screening!.hall.capacity,
        });
        this.updateQuantity(this.quantity());
        this.isLoadingAvailability.set(false);
      },
    });
  }

  private resetState(): void {
    this.currentStep.set('tickets');
    this.quantity.set(1);
    this.isSubmitting.set(false);
    this.availability.set(null);
    this.isLoadingAvailability.set(false);
  }
}
