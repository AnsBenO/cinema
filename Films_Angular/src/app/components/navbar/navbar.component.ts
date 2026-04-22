import { CommonModule } from '@angular/common';
import {
  faBars,
  faCartShopping,
  faXmark,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import {
  Component,
  DestroyRef,
  HostListener,
  inject,
  signal,
  WritableSignal,
} from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import {
  NotificationStore,
  NotificationType,
} from '../../store/notification.store';

@Component({
  standalone: true,
  selector: 'app-navbar',
  imports: [CommonModule, RouterModule, FontAwesomeModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  destroy = inject(DestroyRef);
  router = inject(Router);
  authService = inject(AuthService);
  notificationStore = inject(NotificationStore);

  isMenuOpen: WritableSignal<boolean> = signal(false);

  menuIcon = faBars;
  xMarkIcon = faXmark;
  cartIcon = faCartShopping;

  toggleMenu() {
    this.isMenuOpen.set(!this.isMenuOpen());
  }

  toggleMenuOutsideClick($event: MouseEvent) {
    const menuDiv = document.querySelector('#menuDiv');
    const targetElement = $event.target as HTMLElement;
    if (!menuDiv?.contains(targetElement)) {
      this.toggleMenu();
    }
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.notificationStore.notify(
          'Logged Out Successfully',
          NotificationType.SUCCESS
        );
      },
    });
  }

  @HostListener('document:scroll')
  handleScrolling() {
    this.isMenuOpen.set(false);
  }
}
