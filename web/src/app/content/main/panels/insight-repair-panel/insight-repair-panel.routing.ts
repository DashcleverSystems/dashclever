import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InsightRepairPanelComponent } from './insight-repair-panel.component';
import { PlanningComponent } from './planning/planning.component';
import { EstimateCatalogueComponent } from './estimate-catalogue/estimate-catalogue.component';
import {PlansComponent} from "@content/main/panels/insight-repair-panel/plans/plans.component";

const routes: Routes = [
  {
    path: '',
    component: InsightRepairPanelComponent,
    children: [
      {
        path: 'planning',
        component: PlansComponent,
      },
      {
        path: 'estimate-catalogue',
        component: EstimateCatalogueComponent,
      },
    ],
  },
  {
    path: 'planning/:id/plan',
    component: PlanningComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class InsightRepairPanelRoutingModule {}
