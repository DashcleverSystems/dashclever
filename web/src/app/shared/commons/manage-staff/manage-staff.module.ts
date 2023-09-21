import { NgModule } from '@angular/core';

import {EmployeeFormComponent} from "@shared/commons/manage-staff/employee-form/employee-form.component";
import {ManageStaffStore} from "@shared/commons/manage-staff/manage-staff.store";
import {ManageStaffComponent} from "@shared/commons/manage-staff/manage-staff.component";
import {CommonModule} from "@angular/common";
import {PrimeModule} from "@shared/prime.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";
import {AccessesModule} from "@shared/commons/accesses-selector/accesses-selector.module";

@NgModule({
    declarations: [
        ManageStaffComponent,
        EmployeeFormComponent,
    ],
    imports: [
        CommonModule,
        PrimeModule,
        FormsModule,
        ReactiveFormsModule,
        TranslateModule,
        AccessesModule,
    ],
    exports: [
        ManageStaffComponent,
    ],
    providers: [
        ManageStaffStore,
    ],
})
export class ManageStaffModule {}
