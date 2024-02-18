import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrl: './dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppDialogComponent {
  @Input() visible = true;
  @Input() handyScroll = false;
  @Input() showHeader = true;
  @Input() showFooter = true;
  @Input() closable = false;
  @Input() showCancel = true;

  constructor(
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
  ) {}

  close(): void {
    this.ref.close();
  }

  get contentStyleClass(): string {
    const classNames = ['app-modal-dialog'];

    if (!this.showHeader) {
      classNames.push('no-header');
    }

    if (!this.showFooter) {
      classNames.push('no-footer');
    }

    if (this.handyScroll) {
      classNames.push('handy-scroll-body');
    }

    return classNames.join(' ');
  }
}
