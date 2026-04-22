import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Screening } from '../../models/screening.model';
import {
  faCalendarXmark,
  faChevronLeft,
  faChevronRight,
  faClock,
  faFilm,
  faTimeline,
} from '@fortawesome/free-solid-svg-icons';
import { DatePipe } from '@angular/common';
import { Carousel, CarouselModule } from 'primeng/carousel';
import { environment } from '../../../environment/environment';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

@Component({
  selector: 'app-upcoming-carousel',
  standalone: true,
  imports: [
    FontAwesomeModule,
    DatePipe,
    CarouselModule,
    TagModule,
    ButtonModule,
  ],
  templateUrl: './upcoming-carousel.component.html',
  styleUrl: './upcoming-carousel.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UpcomingCarouselComponent {
  @Input() screeningsList!: Screening[];

  serverUrl = environment.SERVER_URL;

  timeIcon = faTimeline;

  clockIcon = faClock;

  calendarIcon = faCalendarXmark;

  hallIcon = faFilm;

  nextIcon = faChevronRight;

  previousIcon = faChevronLeft;

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
}
