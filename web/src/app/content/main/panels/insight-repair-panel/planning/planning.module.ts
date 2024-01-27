import { NgModule } from '@angular/core';
import { AccessesModule } from '@app/content/main/home/accesses-selector/accesses-selector.module';
import { PrimeModule } from '@shared/prime.module';
import { AppCardComponent } from '@shared/commons/card/card.component';
import { PlanningPageComponent } from '@app/content/main/panels/insight-repair-panel/planning/planning-page/planning-page.component';
import { PlanningComponent } from '@app/content/main/panels/insight-repair-panel/planning/planning.component';
import { CreatePlanningConfirmationDialog } from '@app/content/main/panels/insight-repair-panel/planning/create-confirmation-dialog/create-planning.component';
import { AppSpinner } from '@shared/commons/spinner/spinner.component';
import { SharedModule } from '@app/shared/shared.module';

@NgModule({
  declarations: [
    CreatePlanningConfirmationDialog,
    PlanningPageComponent,
    PlanningComponent,
  ],
  imports: [SharedModule, PrimeModule, AppSpinner, AppCardComponent],
  exports: [PlanningComponent],
  providers: [],
})
export class PlanningModule {}
