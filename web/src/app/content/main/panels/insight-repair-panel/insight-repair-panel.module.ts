import { NgModule } from '@angular/core';
import { InsightRepairPanelComponent } from '@content/main/panels/insight-repair-panel/insight-repair-panel.component';
import { EstimateCatalogueModule } from '@content/main/panels/insight-repair-panel/estimate-catalogue/estimate-catalogue.module';
import { PlanningModule } from '@content/main/panels/insight-repair-panel/planning/planning.module';
import { CommonsModule } from '@shared/commons/commons.module';
import { TranslateModule } from '@ngx-translate/core';
import { InsightRepairPanelRoutingModule } from '@content/main/panels/insight-repair-panel/insight-repair-panel-routing.module';

@NgModule({
  declarations: [InsightRepairPanelComponent],
  imports: [
    CommonsModule,
    TranslateModule,
    InsightRepairPanelRoutingModule,
    EstimateCatalogueModule,
    PlanningModule,
  ],
  providers: [],
})
export class InsightRepairPanelModule {}
