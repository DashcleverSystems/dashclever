import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { ManagePanelComponent } from '@content/main/panels/manage-panel/manage-panel.component';

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
