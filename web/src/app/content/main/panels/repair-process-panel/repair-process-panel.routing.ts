import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RepairProcessPanelComponent } from './repair-process-panel.component';

const routes: Routes = [
  {
    path: '',
    component: RepairProcessPanelComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class RepairProcessPanelRoutingModule {}
