export interface FilmsResponse {
  _embedded: FilmsResponseEmbedded;
  _links: FilmsResponseLinks;
  page: Page;
}

export interface FilmsResponseEmbedded {
  films: Film[];
}

export interface Screening {
  _links: ScreeningLinks;
  id: number;
  film: Film;
  hall: Hall;
  startTime: Date;
  endTime: Date;
}

export interface FilmEmbedded {
  screenings: Screening[];
}

export interface Film {
  createdAt: Date;
  updatedAt: Date;
  title: string;
  duration: number;
  imageUrl: string;
  description: string;
  year: number;
  _embedded?: FilmEmbedded;
  _links?: FilmLinks;
}

export interface ScreeningLinks {
  hall: Profile;
  film: Profile;
  self: Screenings;
}

export interface Profile {
  href: string;
}

export interface Screenings {
  href: string;
  templated: boolean;
}

export interface Hall {
  createdAt: Date;
  updatedAt: Date;
  number: number;
  capacity: number;
}

export interface FilmLinks {
  self: Profile;
  film: Profile;
  director: Profile;
  actors: Profile;
  nationality: Profile;
  screenings: Screenings;
  medias: Profile;
  ratings: Profile;
  genre: Profile;
}

export interface FilmsResponseLinks {
  self: Profile;
  profile: Profile;
  search: Profile;
}

export interface Page {
  size: number;
  totalElements: number;
  totalPages: number;
  number: number;
}
