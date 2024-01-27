import { NgModule } from '@angular/core';
import { PlanningPageComponent } from '@content/main/panels/insight-repair-panel/planning/planning-page/planning-page.component';
import { PlanningComponent } from '@content/main/panels/insight-repair-panel/planning/planning.component';
import { CreatePlanningConfirmationDialog } from '@content/main/panels/insight-repair-panel/planning/create-confirmation-dialog/create-planning.component';
import { CommonsModule } from '@shared/commons/commons.module';

@NgModule({
  declarations: [
    CreatePlanningConfirmationDialog,
    PlanningPageComponent,
    PlanningComponent,
  ],
  imports: [CommonsModule],
  exports: [PlanningComponent],
  providers: [],
})
export class PlanningModule {}
