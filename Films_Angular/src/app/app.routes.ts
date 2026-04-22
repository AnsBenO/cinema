import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home',
  },
  {
    path: 'home',
    title: 'FilmSpot - Home',
    loadComponent: () =>
      import('./pages/home/home.component').then((m) => m.HomeComponent),
  },

  {
    path: 'films',
    title: 'FilmSpot - Films',
    loadComponent: () =>
      import('./pages/film/films/films.component').then(
        (m) => m.FilmsComponent
      ),
  },

  {
    path: 'login',
    loadComponent: () =>
      import('./pages/auth/login/login.component').then(
        (m) => m.LoginComponent
      ),
    title: 'FilmSpot - login',
    canActivate: [authGuard],
  },
  {
    path: 'signup',
    loadComponent: () =>
      import('./pages/auth/signup/signup.component').then(
        (m) => m.SignupComponent
      ),
    title: 'FilmSpot - signup',
    canActivate: [authGuard],
  },
  {
    path: '**',
    title: 'FilmSpot - 404',
    loadComponent: () =>
      import('./pages/error-page/error-page/error-page.component').then(
        (m) => m.ErrorPageComponent
      ),
  },
];
