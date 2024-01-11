import { NgModule } from '@angular/core';
import { EstimatePageComponent } from '@shared/commons/estimate-catalogue/estimate-page/estimate-page.component';
import { EstimateCatalogueComponent } from '@shared/commons/estimate-catalogue/estimate-catalogue.component';
import { EstimateFormComponent } from '@shared/commons/estimate-catalogue/estimate-form/estimate-form.component';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '@shared/prime.module';
import { PaginatorModule } from 'primeng/paginator';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { AppCardComponent } from '@shared/commons/card/card.component';
import { AppSpinner } from '@shared/commons/spinner/spinner.component';
import { InputTextModule } from 'primeng/inputtext';
import { EstimateCreateComponent } from '@shared/commons/estimate-catalogue/estimate-create/estimate-create.component';
import { CalendarModule } from 'primeng/calendar';

@NgModule({
  declarations: [
    EstimateCreateComponent,
    EstimatePageComponent,
    EstimateCatalogueComponent,
    EstimateFormComponent,
  ],
  imports: [
    CommonModule,
    PrimeModule,
    PaginatorModule,
    FormsModule,
    ReactiveFormsModule,
    CalendarModule,
    TranslateModule,
    AppCardComponent,
    AppSpinner,
    InputTextModule,
  ],
  exports: [EstimateCatalogueComponent],
  providers: [],
})
export class EstimateCatalogueModule {}
