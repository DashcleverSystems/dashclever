import { NgModule } from '@angular/core';
import { AccessesSelectorComponent } from './accesses-selector.component';
import { UserSelectorComponent } from './selectors/user-selector';
import { PrimeModule } from '@shared/prime.module';
import { WorkshopCreatedNotifier } from '@app/content/main/home/workshop-creator/workshop-creator.service';
import { SharedModule } from '@app/shared/shared.module';
import { WorkshopCreatorModule } from '../workshop-creator/workshop-creator.module';
import { WorkshopSelectorComponent } from './selectors/workshop-selector';

@NgModule({
  declarations: [
    AccessesSelectorComponent,
    UserSelectorComponent,
    WorkshopSelectorComponent,
  ],
  exports: [
    AccessesSelectorComponent,
    WorkshopSelectorComponent,
    UserSelectorComponent,
  ],
  imports: [SharedModule, PrimeModule, WorkshopCreatorModule],
  providers: [WorkshopCreatedNotifier],
})
export class AccessesModule {}
