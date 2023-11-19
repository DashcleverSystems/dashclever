import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  ViewChild,
} from '@angular/core';
import { PrimeModule } from '@app/shared/prime.module';

@Component({
  selector: 'app-spinner',
  standalone: true,
  imports: [PrimeModule, CommonModule],
  template: `
    <p-progressSpinner
      #progressSpinner
      [ngStyle]="{ display: enable ? 'block' : 'none' }"
      animationDuration="3s"
    ></p-progressSpinner>
    <ng-content *ngIf="!enable"></ng-content>
  `,
  styles: [
    `
      :host::ng-deep {
        .p-progress-spinner {
          width: 50px;
          height: 50px;
          display: flex;
        }
      }
    `,
  ],
})
export class AppSpinner implements AfterViewInit {
  @ViewChild('progressSpinner', { read: ElementRef }) progressSpinner:
    | ElementRef
    | undefined;

  @Input() enable: boolean = false;

  ngAfterViewInit(): void {
    if (this.progressSpinner) {
      const svg = (
        this.progressSpinner.nativeElement as HTMLElement
      ).getElementsByClassName('p-progress-spinner-svg')[0];
      const circle = (
        this.progressSpinner.nativeElement as HTMLElement
      ).getElementsByClassName('p-progress-spinner-circle')[0];

      if (svg && circle) {
        svg.classList.add('icon', 'icon--car');
        svg.removeChild(circle);
      }
    }
  }
}
