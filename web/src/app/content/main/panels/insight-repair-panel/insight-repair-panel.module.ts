import { NgModule } from '@angular/core';
import { InsightRepairPanelComponent } from './insight-repair-panel.component';
import { InsightRepairPanelRoutingModule } from './insight-repair-panel.routing';
import { SharedModule } from '@app/shared/shared.module';
import { EstimateCatalogueModule } from './estimate-catalogue/estimate-catalogue.module';
import { PlanningModule } from './planning/planning.module';
import { InsightRepairAssignModule } from './insight-repair-assign/insight-repair-assign.module';

@NgModule({
  imports: [
    SharedModule,
    InsightRepairPanelRoutingModule,
    EstimateCatalogueModule,
    PlanningModule,
    InsightRepairAssignModule,
  ],
  declarations: [InsightRepairPanelComponent],
})
export class InsightRepairPanelModule {}
