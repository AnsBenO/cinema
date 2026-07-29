import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnChanges,
  SimpleChanges,
  inject,
  signal,
} from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Screening } from '../../models/screening.model';
import {
  faCalendarCheck,
  faCalendarXmark,
  faChevronLeft,
  faChevronRight,
  faClock,
  faFilm,
  faTimeline,
} from '@fortawesome/free-solid-svg-icons';
import { DatePipe } from '@angular/common';
import { CarouselModule } from 'primeng/carousel';
import { environment } from '../../../environment/environment';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { BookingService } from '../../services/booking.service';
import {
  NotificationStore,
  NotificationType,
} from '../../store/notification.store';
import { ScreeningAvailability } from '../../models/booking.model';
import { catchError, forkJoin, of } from 'rxjs';

@Component({
  selector: 'app-upcoming-carousel',
  standalone: true,
  imports: [
    FontAwesomeModule,
    DatePipe,
    CarouselModule,
    TagModule,
    ButtonModule,
    FormsModule,
  ],
  templateUrl: './upcoming-carousel.component.html',
  styleUrl: './upcoming-carousel.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UpcomingCarouselComponent implements OnChanges {
  private readonly authService = inject(AuthService);
  private readonly bookingService = inject(BookingService);
  private readonly notificationStore = inject(NotificationStore);

  @Input() screeningsList!: Screening[];

  readonly user$ = this.authService.user$;

  serverUrl = environment.SERVER_URL;

  calendarCheckIcon = faCalendarCheck;

  timeIcon = faTimeline;

  clockIcon = faClock;

  calendarIcon = faCalendarXmark;

  hallIcon = faFilm;

  nextIcon = faChevronRight;

  previousIcon = faChevronLeft;

  availabilityByScreening = signal<Record<number, ScreeningAvailability>>({});

  quantityByScreening = signal<Record<number, number>>({});

  bookingInProgress = signal<Record<number, boolean>>({});

  responsiveOptions = [
    {
      breakpoint: '1400px',
      numVisible: 3,
      numScroll: 1,
    },
    {
      breakpoint: '1024px',
      numVisible: 2,
      numScroll: 1,
    },
    {
      breakpoint: '768px',
      numVisible: 1,
      numScroll: 1,
    },
    {
      breakpoint: '560px',
      numVisible: 1,
      numScroll: 1,
    },
  ];

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['screeningsList'] && this.screeningsList?.length) {
      this.initializeQuantity();
      this.loadAvailability();
    }
  }

  private initializeQuantity(): void {
    const quantityState: Record<number, number> = {};
    this.screeningsList.forEach((screening) => {
      quantityState[screening.id] =
        this.quantityByScreening()[screening.id] ?? 1;
    });
    this.quantityByScreening.set(quantityState);
  }

  private loadAvailability(): void {
    const requests = this.screeningsList.map((screening) =>
      this.bookingService.getAvailability(screening.id).pipe(
        catchError(() =>
          of({
            screeningId: screening.id,
            hallCapacity: screening.hall.capacity,
            reservedSeats: 0,
            remainingSeats: screening.hall.capacity,
          }),
        ),
      ),
    );

    forkJoin(requests).subscribe((responses) => {
      const state: Record<number, ScreeningAvailability> = {};
      responses.forEach((availability) => {
        state[availability.screeningId] = availability;
      });
      this.availabilityByScreening.set(state);
    });
  }

  getQuantity(screeningId: number): number {
    return this.quantityByScreening()[screeningId] ?? 1;
  }

  getRemainingSeats(
    screening: Screening | null | undefined,
  ): number | undefined {
    if (!screening) {
      return undefined;
    }
    return this.availabilityByScreening()[screening.id]?.remainingSeats;
  }

  isSoldOut(screening: Screening | null | undefined): boolean {
    const remaining = this.getRemainingSeats(screening);
    return remaining === 0;
  }

  isBookingInProgress(screeningId: number | null | undefined): boolean {
    if (!screeningId) {
      return false;
    }
    return !!this.bookingInProgress()[screeningId];
  }

  updateQuantity(screeningId: number, quantity: number): void {
    const parsed = Number.isFinite(quantity) ? Math.floor(quantity) : 1;
    const sanitized = Math.max(1, parsed);
    this.quantityByScreening.update((state) => ({
      ...state,
      [screeningId]: sanitized,
    }));
  }

  bookTickets(screeningId: number): void {
    const quantity = this.getQuantity(screeningId);
    this.bookingInProgress.update((state) => ({
      ...state,
      [screeningId]: true,
    }));

    this.bookingService.createBooking({ screeningId, quantity }).subscribe({
      next: () => {
        this.notificationStore.notify(
          'Booking created successfully',
          NotificationType.SUCCESS,
        );
        this.bookingInProgress.update((state) => ({
          ...state,
          [screeningId]: false,
        }));
        this.loadAvailability();
      },
      error: (error) => {
        const message =
          error?.error?.error ||
          error?.error?.message ||
          'Could not create booking';
        this.notificationStore.notify(message, NotificationType.ERROR);
        this.bookingInProgress.update((state) => ({
          ...state,
          [screeningId]: false,
        }));
      },
    });
  }
}
