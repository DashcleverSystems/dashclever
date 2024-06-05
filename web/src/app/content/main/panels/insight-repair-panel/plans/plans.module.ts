import {NgModule} from '@angular/core';
import {PrimeModule} from '@shared/prime.module';
import {AppCardComponent} from '@shared/commons/card/card.component';
import {AppSpinner} from '@shared/commons/spinner/spinner.component';
import {SharedModule} from '@app/shared/shared.module';
import {CreatePlanConfirmationDialog} from "@content/main/panels/insight-repair-panel/plans/create-confirmation-dialog/create-plan.component";
import {PlansComponent} from "@content/main/panels/insight-repair-panel/plans/plans.component";
import {PlansPagingComponent} from "@content/main/panels/insight-repair-panel/plans/plans-paging/plans-paging.component";

@NgModule({
  declarations: [
    PlansPagingComponent,
    CreatePlanConfirmationDialog,
    PlansComponent,
  ],
  imports: [SharedModule, PrimeModule, AppSpinner, AppCardComponent],
  exports: [PlansComponent],
  providers: [],
})
export class PlansModule {
}
