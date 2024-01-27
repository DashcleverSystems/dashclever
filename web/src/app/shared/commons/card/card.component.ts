import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-card',
  template: `
    <div class="info">{{ title | translate }}</div>
    <div class="card-container">
      <ng-content></ng-content>
    </div>
  `,
  styles: [
    `
      .card-container {
        width: fit-content;
        height: fit-content;
        border-radius: 10px;
        padding: 1rem;
        border: 1px solid rgba($color: #a4c3b2, $alpha: 1);
        box-shadow: 4px 10px 12px -9px rgba($color:
              rgba($color: #6b9080, $alpha: 1), $alpha: 0.69);
        -webkit-box-shadow: 4px 10px 12px -9px rgba($color:
              rgba($color: #6b9080, $alpha: 1), $alpha: 0.69);
        -moz-box-shadow: 4px 10px 12px -9px rgba($color:
              rgba($color: #6b9080, $alpha: 1), $alpha: 0.69);
      }
    `,
  ],
})
export class AppCardComponent {
  @Input() title: string = '';
}
