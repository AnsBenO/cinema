export interface GenreResponse {
  _embedded: Embedded;
  _links: Links;
}

export interface Embedded {
  genre: Genre[];
}

export interface Genre {
  createdAt: Date;
  updatedAt: Date;
  label: string;
  _links: Links;
}

export interface Links {
  additionalProp1: AdditionalProp;
  additionalProp2: AdditionalProp;
  additionalProp3: AdditionalProp;
}

export interface AdditionalProp {
  href: string;
  hreflang: string;
  title: string;
  type: string;
  deprecation: string;
  profile: string;
  name: string;
  templated: boolean;
}
