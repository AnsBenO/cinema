import {
  Component,
  EventEmitter,
  inject,
  Input,
  Output,
  signal,
  computed,
  OnChanges,
  SimpleChanges,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Dialog } from 'primeng/dialog';
import { Rating } from 'primeng/rating';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';

import { FilmService } from '../../../services/film.service';
import { Film } from '../../../models/film.model';
import { environment } from '../../../../environment/environment';
import {
  NotificationStore,
  NotificationType,
} from '../../../store/notification.store';

@Component({
  selector: 'app-film-details',
  standalone: true,
  imports: [CommonModule, FormsModule, Dialog, Rating, FontAwesomeModule],
  templateUrl: 'film-details.component.html',
  styleUrl: 'film-details.component.scss',
})
export class FilmDetailsComponent implements OnChanges {
  private readonly filmService = inject(FilmService);
  private readonly notificationStore = inject(NotificationStore);

  @Input({ required: true }) visible!: boolean;
  @Output() visibleChange = new EventEmitter<boolean>();
  @Input({ required: true }) film!: Film | null;

  readonly closeIcon = faXmark;
  readonly serverUrl = environment.SERVER_URL;

  // Use signals for better reactivity
  score = signal(0);
  isSubmitting = signal(false);

  // Computed signals
  hasFilm = computed(() => !!this.film);
  hasValidScore = computed(() => this.score() > 0);

  ngOnChanges(changes: SimpleChanges) {
    // Reset score when film changes
    if (changes['film'] && this.film) {
      this.score.set(0);
    }
  }

  async submitRating(): Promise<void> {
    if (!this.film || !this.hasValidScore()) {
      this.notificationStore.notify(
        'Please select a rating',
        NotificationType.INFO
      );
      return;
    }

    this.isSubmitting.set(true);

    try {
      await this.filmService
        .rateFilm(this.film.title, this.score())
        .toPromise();
      this.notificationStore.notify(
        'Rating submitted successfully',
        NotificationType.SUCCESS
      );
      this.closeDialog();
    } catch (error) {
      this.notificationStore.notify(
        'You need to signup first',
        NotificationType.ERROR
      );
    } finally {
      this.isSubmitting.set(false);
    }
  }

  closeDialog(): void {
    this.visible = false;
    this.visibleChange.emit(this.visible);
    this.score.set(0);
  }

  // Helper method to prevent dialog from closing when clicking inside
  onContentClick(event: Event): void {
    event.stopPropagation();
  }
}
