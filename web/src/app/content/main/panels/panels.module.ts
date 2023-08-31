import { NgModule } from '@angular/core';
import { ManagePanelComponent } from './manage-panel/manage-panel.component';
import { InsightRepairPanelComponent } from './insight-repair-panel/insight-repair-panel.component';
import { RepairProcessPanelComponent } from './repair-process-panel/repair-process-panel.component';

@NgModule({
  imports: [],
  exports: [],
  declarations: [
    ManagePanelComponent,
    InsightRepairPanelComponent,
    RepairProcessPanelComponent,
  ],
})
export class PanelsModule {}
