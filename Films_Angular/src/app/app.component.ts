import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

import { LoaderComponent } from './components/shared/loader/loader.component';
import { NotificationComponent } from './components/shared/notification/notification.component';
import { NavbarComponent } from './components/shared/navbar/navbar.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    NavbarComponent,
    LoaderComponent,
    NotificationComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'Films_Angular';
  router = inject(Router);
}
