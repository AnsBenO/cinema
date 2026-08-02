import {
  ChangeDetectionStrategy,
  Component,
  HostListener,
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
import { CommonModule, DatePipe } from '@angular/common';
import { CarouselModule } from 'primeng/carousel';
import { environment } from '../../../environment/environment';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { FormsModule } from '@angular/forms';
import { BookingService } from '../../services/booking.service';
import { ScreeningAvailability } from '../../models/booking.model';
import { catchError, forkJoin, of } from 'rxjs';
import { BookTicketComponent } from '../book-ticket/book-ticket.component';

@Component({
  selector: 'app-upcoming-carousel',
  imports: [
    CommonModule,
    FontAwesomeModule,
    DatePipe,
    CarouselModule,
    TagModule,
    ButtonModule,
    FormsModule,
    BookTicketComponent,
  ],
  templateUrl: './upcoming-carousel.component.html',
  styleUrl: './upcoming-carousel.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UpcomingCarouselComponent implements OnChanges {
  private readonly bookingService = inject(BookingService);

  @Input() screeningsList!: Screening[];

  serverUrl = environment.SERVER_URL;

  calendarCheckIcon = faCalendarCheck;

  timeIcon = faTimeline;

  clockIcon = faClock;

  calendarIcon = faCalendarXmark;

  hallIcon = faFilm;

  nextIcon = faChevronRight;

  previousIcon = faChevronLeft;

  slidesPerPage = signal(this.computeSlidesPerPage());

  @HostListener('window:resize')
  onResize(): void {
    this.slidesPerPage.set(this.computeSlidesPerPage());
  }

  private computeSlidesPerPage(): number {
    if (typeof window === 'undefined') {
      return 1;
    }
    const width = window.innerWidth;
    if (width >= 1400) return 3;
    if (width >= 1024) return 2;
    return 1;
  }

  availabilityByScreening = signal<Record<number, ScreeningAvailability>>({});

  selectedScreening = signal<Screening | null>(null);

  bookingDialogVisible = signal(false);

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
      this.loadAvailability();
    }
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

  openBookingDialog(screening: Screening): void {
    this.selectedScreening.set(screening);
    this.bookingDialogVisible.set(true);
  }

  onBookingDialogVisibleChange(visible: boolean): void {
    this.bookingDialogVisible.set(visible);
    if (!visible) {
      this.selectedScreening.set(null);
    }
  }

  onBookingCompleted(): void {
    this.loadAvailability();
  }
}
