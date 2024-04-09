import { Component, Input } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [TranslateModule],
  template: `
    <div class="info">{{ title | translate }}</div>
    <div class="card-container">
      <ng-content></ng-content>
    </div>
  `,
  styles: [
    `
      @import 'src/assets/styles/variables';
      @import 'src/assets/styles/mixins';

      .card-container {
        width: fit-content;
        height: fit-content;
        border-radius: 10px;
        padding: 1rem;
        border: 1px solid var(--border-color);
        @include shadow();
      }
    `,
  ],
})
export class AppCardComponent {
  @Input() title: string = '';
}
