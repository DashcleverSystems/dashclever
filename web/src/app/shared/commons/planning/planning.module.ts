import { NgModule } from '@angular/core';
import { AccessesModule } from '@shared/commons/accesses-selector/accesses-selector.module';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '@shared/prime.module';
import { PaginatorModule } from 'primeng/paginator';
import { TranslateModule } from '@ngx-translate/core';
import { ManageStaffModule } from '@shared/commons/manage-staff/manage-staff.module';
import { AppCardComponent } from '@shared/commons/card/card.component';
import { PlanningPageComponent } from '@shared/commons/planning/planning-page/planning-page.component';
import { CalendarModule } from 'primeng/calendar';
import { PlanningComponent } from '@shared/commons/planning/planning.component';

@NgModule({
  declarations: [PlanningPageComponent, PlanningComponent],
  imports: [
    AccessesModule,
    CommonModule,
    PrimeModule,
    PaginatorModule,
    TranslateModule,
    ManageStaffModule,
    AppCardComponent,
    CalendarModule,
  ],
  exports: [PlanningComponent],
  providers: [],
})
export class PlanningModule {}
