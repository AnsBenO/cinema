import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faEnvelope,
  faLock,
  faPhone,
  faUser,
  faGlobe,
  faFilm,
} from '@fortawesome/free-solid-svg-icons';
import { matchPasswords } from './match-passwords.validator';
import { Router, RouterModule } from '@angular/router';
import { TrimInputDirective } from '../trim-input.directive';
import {
  NotificationStore,
  NotificationType,
} from '../../../store/notification.store';
import { RegisterPayload } from '../../../models/user.model';

@Component({
  standalone: true,
  selector: 'app-signup',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TrimInputDirective,
    FontAwesomeModule,
    RouterModule,
  ],
  templateUrl: './signup.component.html',
  styleUrl: '../auth.css',
})
export class SignupComponent {
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  destroy = inject(DestroyRef);
  router = inject(Router);
  filmIcon = faFilm;
  notificationStore = inject(NotificationStore);
  maxLengths = {
    username: 20,
    password: 8,
  };

  minLengths = {
    username: 3,
    password: 8,
  };

  signupForm = this.fb.nonNullable.group(
    {
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(this.minLengths.username),
          Validators.maxLength(this.maxLengths.username),
        ],
      ],
      password: [
        '',
        [Validators.required, Validators.minLength(this.minLengths.password)],
      ],
      confirmPassword: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
    },
    {
      validators: matchPasswords,
    }
  );
  pswIcon = faLock;
  userIcon = faUser;
  emailIcon = faEnvelope;
  phoneIcon = faPhone;
  countryIcon = faGlobe;

  onSubmit() {
    const dataToSubmit = <RegisterPayload>this.signupForm.getRawValue();
    delete dataToSubmit.confirmPassword;
    this.authService
      .register(dataToSubmit)
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe({
        next: () => {
          this.notificationStore.notify(
            'You are now registered',
            NotificationType.SUCCESS
          );
          this.router.navigate(['/']);
        },
      });
  }

  public isInvalidInput(inputName: string): boolean {
    return !!(
      this.signupForm.get(inputName)?.invalid &&
      this.isDirtyAndTouched(inputName)
    );
  }

  public isDirtyAndTouched(inputName: string): boolean {
    return !!(
      this.signupForm.get(inputName)?.dirty &&
      this.signupForm.get(inputName)?.touched
    );
  }
}
