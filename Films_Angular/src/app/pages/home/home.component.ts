import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { ScreeningService } from '../../services/screening.service';
import { AsyncPipe, CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { environment } from '../../../environment/environment';
import { RouterModule } from '@angular/router';
import { faArrowDown } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { UpcomingCarouselComponent } from '../../components/upcoming-carousel/upcoming-carousel.component';

@Component({
  standalone: true,
  selector: 'app-home',
  imports: [
    CommonModule,
    AsyncPipe,
    RouterModule,
    FontAwesomeModule,
    UpcomingCarouselComponent,
  ],
  providers: [ScreeningService],

  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  screeningService = inject(ScreeningService);

  downArrowIcon = faArrowDown;

  @ViewChild('upcomingScreenings') upcomingScreenings!: ElementRef;
  scrollToUpcomingScreenings() {
    this.upcomingScreenings.nativeElement.scrollIntoView({
      behavior: 'smooth',
    });
  }
}
