import { Injectable, signal, WritableSignal, inject } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import {
  AuthResponse,
  CurrentUser,
  LoginPayload,
  LogoutResponse,
} from '../models/user.model';
import {
  catchError,
  filter,
  firstValueFrom,
  Observable,
  of,
  take,
  tap,
  throwError,
} from 'rxjs';
import { environment } from '../../environment/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly currentUser: WritableSignal<CurrentUser | null | undefined> =
    signal(undefined);
  user$ = toObservable(this.currentUser);

  http = inject(HttpClient);

  initializeUser(): Promise<unknown> {
    if (localStorage.getItem('accessToken')) {
      return firstValueFrom(this.getCurrentUser());
    }
    return Promise.resolve();
  }

  getCurrentUser(): Observable<CurrentUser> {
    return this.http.post<CurrentUser>(`${environment.API_URL}/user`, {}).pipe(
      tap((response) => {
        this.currentUser.set(response);
        console.log(response);
      }),
      catchError((error) => {
        localStorage.removeItem('accessToken');
        console.error('Failed to load user:', error);
        this.currentUser.set(undefined);
        return throwError(() => error);
      })
    );
  }

  constructor() {
    this.user$
      .pipe(
        filter(
          (user): user is CurrentUser => user !== null && user !== undefined
        ),
        take(1)
      )
      .subscribe();
  }

  login(formData: LoginPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.API_URL}/authenticate`, formData)
      .pipe(
        tap((response) => {
          this.setAccessToken(response.jwt);
          this.currentUser.set(response.user);
        })
      );
  }

  register(formData: CurrentUser): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.API_URL}/register`, formData)
      .pipe(
        tap((response) => {
          this.setAccessToken(response.jwt);
          this.currentUser.set(response.user);
        })
      );
  }

  logout(): Observable<LogoutResponse> {
    localStorage.removeItem('accessToken');
    this.currentUser.set(null);
    return of({ message: 'logged out' });
  }

  setAccessToken(token: string): void {
    localStorage.setItem('accessToken', token);
  }
  setRefreshToken(token: string) {
    localStorage.setItem('refreshToken', token);
  }
}
