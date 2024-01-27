import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InsightRepairPanelComponent } from './insight-repair-panel.component';

const routes: Routes = [
  {
    path: '',
    component: InsightRepairPanelComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class InsightRepairPanelRoutingModule {}
