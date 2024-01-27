import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { RepairProcessPanelRoutingModule } from './repair-process-panel.routing';
import { RepairProcessPanelComponent } from './repair-process-panel.component';

@NgModule({
  imports: [SharedModule, RepairProcessPanelRoutingModule],
  declarations: [RepairProcessPanelComponent],
})
export class RepairProcessPanelModule {}
