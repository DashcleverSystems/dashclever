import { NgModule } from '@angular/core';
import { WorkshopCreatorComponent } from './workshop-creator.component';
import { WorkshopCreatorFormComponent } from './workshop-creator-form/workshop-creator-form.component';
import { SharedModule } from '@app/shared/shared.module';
import { PrimeModule } from '@app/shared/prime.module';

@NgModule({
  imports: [SharedModule, PrimeModule],
  declarations: [WorkshopCreatorComponent, WorkshopCreatorFormComponent],
  exports: [WorkshopCreatorComponent, WorkshopCreatorFormComponent],
})
export class WorkshopCreatorModule {}
