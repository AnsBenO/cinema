import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { faLock, faUser } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  NotificationStore,
  NotificationType,
} from '../../../store/notification.store';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, RouterModule, FontAwesomeModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: '../auth.css',
})
export class LoginComponent {
  pswIcon = faLock;
  userIcon = faUser;
  notificationStore = inject(NotificationStore);
  fb = inject(FormBuilder);
  destroy = inject(DestroyRef);
  authService = inject(AuthService);
  router = inject(Router);

  loginForm = this.fb.nonNullable.group({
    email: ['', Validators.required],
    password: ['', Validators.required],
  });

  onSubmit(): void {
    this.authService
      .login(this.loginForm.getRawValue())
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe({
        next: () => {
          this.router.navigate(['/']);
          this.notificationStore.notify(
            'Logged In Successfully',
            NotificationType.SUCCESS
          );
        },
        error: () => {
          this.notificationStore.notify(
            'Invalid Credentials',
            NotificationType.ERROR
          );
        },
      });
  }
  public isInvalidInput(inputName: string): boolean {
    return !!(
      this.loginForm.get(inputName)?.invalid &&
      this.isDirtyAndTouched(inputName)
    );
  }

  public isDirtyAndTouched(inputName: string): boolean {
    return !!(
      this.loginForm.get(inputName)?.dirty &&
      this.loginForm.get(inputName)?.touched
    );
  }
}
