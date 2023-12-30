import { Component } from '@angular/core';
import { AccessesSelectorComponentStore } from '@shared/commons/accesses-selector/access-selector.store';

@Component({
  selector: 'app-accesses-selector',
  templateUrl: './accesses-selector.component.html',
  styleUrls: ['./accesses-selector.component.scss'],
  providers: [AccessesSelectorComponentStore],
})
export class AccessesSelectorComponent {
  onClick(item: any): void {}
}
