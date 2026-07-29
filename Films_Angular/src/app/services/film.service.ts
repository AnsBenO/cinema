import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  combineLatest,
  debounceTime,
  distinctUntilChanged,
  finalize,
  map,
  Observable,
  switchMap,
  tap,
} from 'rxjs';
import { FilmsResponse } from '../models/film.model';
import { environment } from '../../environment/environment';
import { GenreResponse } from '../models/genre.model';
import { FilmRatingPayload } from '../models/rating.model';

export interface RatingResponse {
  message: string;
}

export interface RatingScore {
  score: number;
}

@Injectable()
export class FilmService {
  http = inject(HttpClient);

  private readonly filmsPage = new BehaviorSubject<number>(1);
  private readonly selectedGenre = new BehaviorSubject<string | null>(null);
  private readonly keyword = new BehaviorSubject<string>('');

  private readonly loadingSubject = new BehaviorSubject<boolean>(false);

  isLoading$ = this.loadingSubject.asObservable();

  // Fetch genres
  genres$ = this.http.get<GenreResponse>(
    `${environment.API_URL}/genres/search/findAllNoPagination`,
  );

  // Combine filters and trigger the films API request
  films$: Observable<FilmsResponse> = combineLatest([
    this.keyword.pipe(debounceTime(300), distinctUntilChanged()),
    this.selectedGenre.pipe(distinctUntilChanged()),
    this.filmsPage.pipe(distinctUntilChanged()),
  ]).pipe(
    tap(() => this.loadingSubject.next(true)),
    switchMap(([keyword, genre, page]) =>
      this.http
        .get<FilmsResponse>(
          `${environment.API_URL}/films/search/findByTitleAndGenre`,
          {
            params: {
              keyword: keyword || '',
              genreLabel: genre ?? '',
              page: page.toString(),
            },
          },
        )
        .pipe(
          // set loading to false when data is retrieved
          finalize(() => this.loadingSubject.next(false)),
        ),
    ),
  );

  rateFilm(film: string, score: number): Observable<RatingResponse> {
    const payload: FilmRatingPayload = {
      film: film,
      score,
    };
    return this.http.post<RatingResponse>(
      `${environment.API_URL}/film-ratings`,
      payload,
    );
  }

  getRating(title: string, userEmail: string): Observable<RatingScore> {
    return this.http
      .get<RatingScore>(
        `${environment.API_URL}/film-ratings/${encodeURIComponent(title)}/${encodeURIComponent(userEmail)}`,
      )
      .pipe(map((response) => response));
  }

  // Methods to update filters
  updateKeyword(keyword: string) {
    this.keyword.next(keyword);
  }

  updateSelectedGenre(genre: string | null) {
    this.selectedGenre.next(genre);
  }

  updateFilmsPage(page: number) {
    const currentPage = this.filmsPage.value;
    this.filmsPage.next(Math.max(1, currentPage + page));
  }
}
