import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../environment/environment';
import {
  Booking,
  CreateBookingPayload,
  ScreeningAvailability,
} from '../models/booking.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BookingService {
  private readonly http = inject(HttpClient);

  createBooking(payload: CreateBookingPayload): Observable<Booking> {
    return this.http.post<Booking>(`${environment.API_URL}/bookings`, payload);
  }

  getMyBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${environment.API_URL}/bookings/me`);
  }

  getAvailability(screeningId: number): Observable<ScreeningAvailability> {
    return this.http.get<ScreeningAvailability>(
      `${environment.API_URL}/bookings/availability/${screeningId}`,
    );
  }

  cancelBooking(bookingId: number): Observable<Booking> {
    return this.http.delete<Booking>(
      `${environment.API_URL}/bookings/${bookingId}`,
    );
  }
}
