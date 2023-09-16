import { NgModule } from '@angular/core';
import { LanguageSelector } from './language-selector/language-selector.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '../prime.module';
import { AccessesModule } from './accesses-selector/accesses-selector.module';
import { AccessesSelectorComponent } from './accesses-selector/accesses-selector.component';
import { ManageStaffComponent } from './manage-staff/manage-staff.component';
import { AppCardComponent } from './card/card.component';

@NgModule({
  declarations: [LanguageSelector, ManageStaffComponent, AppCardComponent],
  imports: [CommonModule, PrimeModule, TranslateModule, AccessesModule],
  exports: [
    LanguageSelector,
    AccessesSelectorComponent,
    ManageStaffComponent,
    AppCardComponent,
  ],
  providers: [],
})
export class CommonsModule {}
