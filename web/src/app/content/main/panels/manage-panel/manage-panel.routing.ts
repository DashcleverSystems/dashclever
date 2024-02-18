import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ManagePanelComponent } from './manage-panel.component';

const routes: Routes = [
  {
    path: '',
    component: ManagePanelComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ManagePanelRoutingModule {}
