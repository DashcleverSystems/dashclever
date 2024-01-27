import { NgModule } from '@angular/core';
import { EstimatePageComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-page/estimate-page.component';
import { EstimateCatalogueComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-catalogue.component';
import { EstimateFormComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-form.component';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '@shared/prime.module';
import { AppCardComponent } from '@shared/commons/card/card.component';
import { AppSpinner } from '@shared/commons/spinner/spinner.component';
import { EstimateCreateComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-create/estimate-create.component';
import { SharedModule } from '@app/shared/shared.module';

@NgModule({
  declarations: [
    EstimateCreateComponent,
    EstimatePageComponent,
    EstimateCatalogueComponent,
    EstimateFormComponent,
  ],
  imports: [SharedModule, PrimeModule, AppCardComponent, AppSpinner],
  exports: [EstimateCatalogueComponent],
  providers: [],
})
export class EstimateCatalogueModule {}
