import { NgModule } from '@angular/core';
import { InsightRepairPanelComponent } from './insight-repair-panel.component';
import { InsightRepairPanelRoutingModule } from './insight-repair-panel.routing';
import { SharedModule } from '@app/shared/shared.module';
import { EstimateCatalogueModule } from './estimate-catalogue/estimate-catalogue.module';
import { PlanningModule } from './planning/planning.module';

@NgModule({
  imports: [
    SharedModule,
    InsightRepairPanelRoutingModule,
    EstimateCatalogueModule,
    PlanningModule,
  ],
  declarations: [InsightRepairPanelComponent],
})
export class InsightRepairPanelModule {}
