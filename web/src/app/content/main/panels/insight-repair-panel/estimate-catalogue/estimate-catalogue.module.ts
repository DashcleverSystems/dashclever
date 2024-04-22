import { NgModule } from '@angular/core';
import { EstimatePageComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-page/estimate-page.component';
import { EstimateCatalogueComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-catalogue.component';
import { EstimateFormComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-form.component';
import { PrimeModule } from '@shared/prime.module';
import { AppCardComponent } from '@shared/commons/card/card.component';
import { EstimateReportFormComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-report-form/estimate-report-form.component';
import { AppSpinner } from '@shared/commons/spinner/spinner.component';
import { EstimateCreateComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-create/estimate-create.component';
import { SharedModule } from '@app/shared/shared.module';
import {
  EstimateReportFormService
} from "@content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-report-form/estimate-report-form.service";

@NgModule({
  declarations: [
    EstimateCreateComponent,
    EstimatePageComponent,
    EstimateCatalogueComponent,
    EstimateFormComponent,
    EstimateReportFormComponent
  ],
  imports: [SharedModule, PrimeModule, AppCardComponent, AppSpinner],
  exports: [EstimateCatalogueComponent],
  providers: [EstimateReportFormService],
})
export class EstimateCatalogueModule {}
