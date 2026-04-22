import { CommonModule } from '@angular/common';
import {
  Component,
  DestroyRef,
  OnInit,
  WritableSignal,
  inject,
  signal,
} from '@angular/core';
import {
  NotificationStore,
  NotificationType,
  TNotification,
} from '../../store/notification.store';

import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import {
  faCircleCheck,
  faCircleXmark,
  faInfoCircle,
  faXmark,
  IconDefinition,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Router } from '@angular/router';
import { filter, tap } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-notification',
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss',
})
export class NotificationComponent implements OnInit {
  notificationStore = inject(NotificationStore);
  destroy = inject(DestroyRef);
  notification$ = toObservable(this.notificationStore.notification);
  router = inject(Router);
  message: WritableSignal<string | null> = signal(null);
  notifications: WritableSignal<TNotification[]> = signal([]);
  infoIcon = faInfoCircle;
  xMarkIcon = faXmark;
  circleXmarkIcon = faCircleXmark;
  circleCheckIcon = faCircleCheck;
  private nextId = 0;

  notificationClassMap = new Map<string, string>([
    ['ERROR', 'bg-red-300 text-red-800'],
    ['INFO', 'bg-blue-200 text-night-dark'],
    ['SUCCESS', 'bg-green-300 text-green-800'],
  ]);

  /**
   * Returns the CSS class to be applied to the notification based on the given type.
   *
   * @param type The type of the notification.
   * @returns The CSS class to be applied.
   */
  getNotificationClass(type: NotificationType | null): string {
    return type ? this.notificationClassMap.get(type) ?? '' : '';
  }

  /**
   * Returns the icon to be displayed for the given notification type.
   *
   * @param type The type of the notification.
   * @returns The icon to be displayed.
   */
  getIcon(type: NotificationType): IconDefinition {
    // Circle Xmark for errors
    // Info Circle for info notifications
    // Circle Check for success notifications
    switch (type) {
      case NotificationType.ERROR:
        return this.circleXmarkIcon;
      case NotificationType.INFO:
        return this.infoIcon;
      case NotificationType.SUCCESS:
        return this.circleCheckIcon;
    }
  }
  /**
   * Initializes the component by subscribing to the notification observable.
   * When a new notification is received, it is added to the list of notifications
   * and a timer is set to remove it after 5 seconds.
   */
  ngOnInit(): void {
    this.notification$
      .pipe(
        takeUntilDestroyed(this.destroy),
        filter((notification): notification is TNotification => {
          // Ensure that the notification has a message and type
          return notification.message !== null && notification.type !== null;
        }),
        tap((notification) => {
          // Get the next available ID
          const id = this.nextId++;
          // Create a new notification with the ID, message, and type
          const newNotification: TNotification = {
            id,
            message: notification.message,
            type: notification.type,
          };
          // Add the new notification to the list of notifications
          this.notifications.set([...this.notifications(), newNotification]);
          // Set a timer to remove the notification after 5 seconds
          setTimeout(() => {
            this.notifications.set(
              this.notifications().filter((n) => n.id !== id)
            );
          }, 5000);
        })
      )
      .subscribe();
  }

  /**
   * Destroys a notification by removing it from the list of notifications.
   * @param id The ID of the notification to destroy.
   */
  destroyNotification(id: number): void {
    // Filter out the notification with the given ID from the list of notifications
    this.notifications.update((list) => list.filter((n) => n.id !== id));
  }
}
