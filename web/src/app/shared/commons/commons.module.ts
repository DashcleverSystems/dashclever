import { NgModule } from '@angular/core';
import { LanguageSelector } from './language-selector/language-selector.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '../prime.module';
import { AppCardComponent } from './card/card.component';
import { AppSpinner } from './spinner/spinner.component';
import { AppDialogComponent } from './dialog/dialog.component';
import { LinearDatePickerComponent } from './date-picker/linear-date-picker.component';
import { CommonFormControlModule } from '@shared/commons/form-control/common-form-control.module';
import { FormControlErrorOutput } from '@shared/commons/form-control/error-output/form-control-error-output.component';

@NgModule({
  declarations: [LanguageSelector, AppDialogComponent],
  imports: [
    CommonModule,
    PrimeModule,
    TranslateModule,
    AppCardComponent,
    AppSpinner,
    LinearDatePickerComponent,
    CommonFormControlModule,
  ],
  exports: [
    LanguageSelector,
    AppCardComponent,
    AppSpinner,
    AppDialogComponent,
    FormControlErrorOutput,
    LinearDatePickerComponent,
  ],
  providers: [],
})
export class CommonsModule {}
