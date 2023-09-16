import { NgModule } from '@angular/core';
import { LanguageSelector } from './language-selector/language-selector.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '../prime.module';
import { AccessesModule } from './accesses-selector/accesses-selector.module';
import { AccessesSelectorComponent } from './accesses-selector/accesses-selector.component';
import { ManageStaffComponent } from './manage-staff/manage-staff.component';
import { AppCardComponent } from './card/card.component';
import { EstimateCatalogueComponent } from './estimate-catalogue/estimate-catalogue.component';
import { EstimateFormComponent } from './estimate-catalogue/estimate-form/estimate-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    LanguageSelector,
    ManageStaffComponent,
    AppCardComponent,
    EstimateCatalogueComponent,
    EstimateFormComponent,
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
    LanguageSelector,
    AccessesSelectorComponent,
    ManageStaffComponent,
    AppCardComponent,
    EstimateCatalogueComponent,
    EstimateFormComponent,
  ],
  providers: [],
})
export class CommonsModule {}
