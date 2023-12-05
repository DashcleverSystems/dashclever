import { NgModule } from '@angular/core';
import { AccessesSelectorComponent } from './accesses-selector.component';
import { WorkshopSelectorComponent } from './selectors/workshop-selector';
import { CommonModule } from '@angular/common';
import { UserSelectorComponent } from './selectors/user-selector';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { WorkshopCreatorFormComponent } from '@shared/commons/workshop/workshop-creator/workshop-creator-form/workshop-creator-form.component';
import { WorkshopCreatorComponent } from '@shared/commons/workshop/workshop-creator/workshop-creator.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PrimeModule } from '@shared/prime.module';
import { WorkshopCreatedNotifier } from '@shared/commons/workshop/workshop-creator/workshop-creator.service';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
  declarations: [
    AccessesSelectorComponent,
    WorkshopSelectorComponent,
    WorkshopCreatorComponent,
    WorkshopCreatorFormComponent,
    UserSelectorComponent,
  ],
  exports: [
    AccessesSelectorComponent,
    WorkshopCreatorComponent,
    WorkshopCreatorFormComponent,
    WorkshopCreatorComponent,
    InputTextModule,
  ],
  imports: [
    CommonModule,
    TranslateModule,
    ButtonModule,
    ReactiveFormsModule,
    FormsModule,
    PrimeModule,
    TooltipModule,
  ],
  providers: [WorkshopCreatedNotifier],
})
export class AccessesModule {}
