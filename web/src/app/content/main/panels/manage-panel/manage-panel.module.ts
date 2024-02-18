import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { ManagePanelComponent } from './manage-panel.component';
import { ManagePanelRoutingModule } from './manage-panel.routing';
import { ManageStaffModule } from './manage-staff/manage-staff.module';

@NgModule({
  imports: [SharedModule, ManagePanelRoutingModule, ManageStaffModule],
  declarations: [ManagePanelComponent],
})
export class ManagePanelModule {}
