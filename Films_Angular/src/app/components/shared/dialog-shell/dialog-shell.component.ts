import { Component, input, model, output } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import { Dialog } from 'primeng/dialog';

@Component({
  selector: 'app-dialog-shell',
  imports: [Dialog, FontAwesomeModule],
  templateUrl: './dialog-shell.component.html',
  styleUrl: './dialog-shell.component.css',
})
export class DialogShellComponent {
  readonly visible = model.required<boolean>();
  readonly width = input.required<string>();
  readonly closeOnEscape = input(true);
  readonly closeDisabled = input(false);
  readonly closed = output<void>();

  readonly closeIcon = faXmark;

  requestClose(): void {
    if (!this.closeDisabled()) {
      this.visible.set(false);
    }
  }

  onDialogHide(): void {
    this.visible.set(false);
    this.closed.emit();
  }
}
