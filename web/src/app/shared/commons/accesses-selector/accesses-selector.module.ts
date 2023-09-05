import { NgModule } from '@angular/core';
import { AccessesSelectorComponent } from './accesses-selector.component';
import { WorkshopSelectorComponent } from './selectors/workshop-selector';
import { CommonModule } from '@angular/common';
import { UserSelectorComponent } from './selectors/user-selector';

@NgModule({
  declarations: [
    AccessesSelectorComponent,
    WorkshopSelectorComponent,
    UserSelectorComponent,
  ],
  exports: [AccessesSelectorComponent],
  imports: [CommonModule],
})
export class AccessesModule {}
