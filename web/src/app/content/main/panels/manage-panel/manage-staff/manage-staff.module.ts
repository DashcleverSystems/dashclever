import { NgModule } from '@angular/core';

import { EmployeeFormComponent } from '@content/main/panels/manage-panel/manage-staff/employee-form/employee-form.component';
import { ManageStaffStore } from '@content/main/panels/manage-panel/manage-staff/manage-staff.store';
import { ManageStaffComponent } from '@content/main/panels/manage-panel/manage-staff/manage-staff.component';
import { CommonsModule } from '@shared/commons/commons.module';

@NgModule({
  declarations: [ManageStaffComponent, EmployeeFormComponent],
  imports: [CommonsModule],
  exports: [ManageStaffComponent],
  providers: [ManageStaffStore],
})
export class ManageStaffModule {}
