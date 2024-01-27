import { NgModule } from '@angular/core';
import { ManagePanelComponent } from '@content/main/panels/manage-panel/manage-panel.component';
import { CommonsModule } from '@shared/commons/commons.module';
import { ManagePanelRoutingModule } from '@content/main/panels/manage-panel/manage-panel-routing.module';
import { ManageStaffModule } from '@content/main/panels/manage-panel/manage-staff/manage-staff.module';

@NgModule({
  declarations: [ManagePanelComponent],
  imports: [CommonsModule, ManagePanelRoutingModule, ManageStaffModule],
  providers: [],
})
export class ManagePanelModule {}
