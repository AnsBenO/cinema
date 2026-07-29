import { Film } from './film.model';

export type ScreeningResponse = Screening[];

export interface Screening {
  id: number;
  createdAt: string;
  updatedAt: string;
  startTime: string; // ISO 8601 datetime string
  endTime: string; // ISO 8601 datetime string
  film: Film;
  hall: Hall;
}

export interface Hall {
  createdAt: string; // ISO 8601 datetime string
  updatedAt: string; // ISO 8601 datetime string
  id: number;
  number: number; // hall number
  capacity: number; // seating capacity
}
