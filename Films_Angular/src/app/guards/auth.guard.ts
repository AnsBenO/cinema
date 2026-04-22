import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map } from 'rxjs';

/**
 * Guard that blocks access to routes if the user is already logged in.
 *
 * If the user is not logged in (i.e. `user$` emits `null` or `undefined`),
 * the guard allows access to the route.
 *
 * If the user is logged in, the guard redirects to the home page (`/`)
 * and blocks access to the route.
 */
export const authGuard: CanActivateFn = (_route, _state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  return authService.user$.pipe(
    map((user) => {
      if (user === null || user === undefined) {
        return true; // Allow access
      } else {
        router.navigateByUrl('/'); // Redirect to home page
        return false; // Block access
      }
    })
  );
};
