export interface Booking {
  id: number;
  screeningId: number;
  filmTitle: string;
  screeningStartTime: string;
  hallNumber: number;
  quantity: number;
  status: BookingStatus;
  bookedAt: string;
}

export type BookingStatus = 'BOOKED' | 'CANCELED';

export interface CreateBookingPayload {
  screeningId: number;
  quantity: number;
}

export interface ScreeningAvailability {
  screeningId: number;
  hallCapacity: number;
  reservedSeats: number;
  remainingSeats: number;
}

export interface ApiErrorResponse {
  error?: string;
  message?: string;
  fieldErrors?: Record<string, string[]>;
}
