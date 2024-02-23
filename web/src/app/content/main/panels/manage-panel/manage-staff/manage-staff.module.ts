import { NgModule } from '@angular/core';

import { EmployeeFormComponent } from '@app/content/main/panels/manage-panel/manage-staff/employee-form/employee-form.component';
import { ManageStaffStore } from '@app/content/main/panels/manage-panel/manage-staff/manage-staff.store';
import { ManageStaffComponent } from '@app/content/main/panels/manage-panel/manage-staff/manage-staff.component';
import { PrimeModule } from '@shared/prime.module';
import { SharedModule } from '@app/shared/shared.module';

@NgModule({
  declarations: [ManageStaffComponent, EmployeeFormComponent],
  imports: [SharedModule, PrimeModule],
  exports: [ManageStaffComponent],
  providers: [ManageStaffStore],
})
export class ManageStaffModule {}
