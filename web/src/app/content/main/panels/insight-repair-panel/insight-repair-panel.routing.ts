import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InsightRepairPanelComponent } from './insight-repair-panel.component';
import { PlanningComponent } from './planning/planning.component';
import { EstimateCatalogueComponent } from './estimate-catalogue/estimate-catalogue.component';

const routes: Routes = [
  {
    path: '',
    component: InsightRepairPanelComponent,
    children: [
      {
        path: 'planning',
        component: PlanningComponent,
      },
      {
        path: 'estimate-catalogue',
        component: EstimateCatalogueComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class InsightRepairPanelRoutingModule {}
