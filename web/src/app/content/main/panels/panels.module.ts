import { NgModule } from '@angular/core';
import { ManagePanelComponent } from './manage-panel/manage-panel.component';
import { InsightRepairPanelComponent } from './insight-repair-panel/insight-repair-panel.component';
import { RepairProcessPanelComponent } from './repair-process-panel/repair-process-panel.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonsModule } from 'src/app/shared/commons/commons.module';

@NgModule({
  imports: [CommonsModule, TranslateModule],
  exports: [],
  declarations: [
    ManagePanelComponent,
    InsightRepairPanelComponent,
    RepairProcessPanelComponent,
  ],
})
export class PanelsModule {}
