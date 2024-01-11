import { NgModule } from '@angular/core';
import { LanguageSelector } from './language-selector/language-selector.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '../prime.module';
import { AccessesModule } from '@shared/commons/accesses-selector/accesses-selector.module';
import { AccessesSelectorComponent } from '@shared/commons/accesses-selector/accesses-selector.component';
import { ManageStaffComponent } from './manage-staff/manage-staff.component';
import { AppCardComponent } from './card/card.component';
import { EstimateCatalogueComponent } from './estimate-catalogue/estimate-catalogue.component';
import { EstimateFormComponent } from './estimate-catalogue/estimate-form/estimate-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppSpinner } from './spinner/spinner.component';
import { ManageStaffModule } from '@shared/commons/manage-staff/manage-staff.module';
import { EstimatePageComponent } from '@shared/commons/estimate-catalogue/estimate-page/estimate-page.component';
import { PaginatorModule } from 'primeng/paginator';
import { CalendarModule } from 'primeng/calendar';
import { InputTextModule } from 'primeng/inputtext';
import { PlanningModule } from '@shared/commons/planning/planning.module';
import { PlanningComponent } from '@shared/commons/planning/planning.component';

@NgModule({
  declarations: [
    LanguageSelector,
    EstimatePageComponent,
    EstimateCatalogueComponent,
    EstimateFormComponent,
  ],
  imports: [
    AccessesModule,
    CommonModule,
    PrimeModule,
    PaginatorModule,
    FormsModule,
    ReactiveFormsModule,
    TranslateModule,
    AccessesModule,
    ManageStaffModule,
    AppCardComponent,
    AppSpinner,
    InputTextModule,
    CalendarModule,
    PlanningModule,
  ],
  exports: [
    LanguageSelector,
    AccessesSelectorComponent,
    ManageStaffComponent,
    EstimateCatalogueComponent,
    EstimatePageComponent,
    EstimateFormComponent,
    PlanningComponent,
  ],
  providers: [],
})
export class CommonsModule {}
