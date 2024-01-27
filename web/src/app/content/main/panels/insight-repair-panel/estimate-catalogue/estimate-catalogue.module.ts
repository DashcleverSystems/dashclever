import { NgModule } from '@angular/core';
import { EstimateCreateComponent } from '@content/main/panels/insight-repair-panel/estimate-catalogue/estimate-create/estimate-create.component';
import { EstimatePageComponent } from '@content/main/panels/insight-repair-panel/estimate-catalogue/estimate-page/estimate-page.component';
import { EstimateCatalogueComponent } from '@content/main/panels/insight-repair-panel/estimate-catalogue/estimate-catalogue.component';
import { EstimateFormComponent } from '@content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-form.component';
import { CommonsModule } from '@shared/commons/commons.module';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [
    EstimateCreateComponent,
    EstimatePageComponent,
    EstimateCatalogueComponent,
    EstimateFormComponent,
  ],
  imports: [CommonsModule, TranslateModule],
  exports: [EstimateCatalogueComponent],
  providers: [],
})
export class EstimateCatalogueModule {}
