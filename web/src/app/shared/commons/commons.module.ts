import { NgModule } from '@angular/core';
import { LanguageSelector } from './language-selector/language-selector.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '../prime.module';
import { AppCardComponent } from './card/card.component';
import { AppSpinner } from './spinner/spinner.component';
import { AppDialogComponent } from './dialog/dialog.component';
import { LinearDatePickerComponent } from './date-picker/linear-date-picker.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [LanguageSelector, AppDialogComponent],
  imports: [
    CommonModule,
    PrimeModule,
    TranslateModule,
    AppCardComponent,
    AppSpinner,
    LinearDatePickerComponent,
    ReactiveFormsModule,
    FormsModule,
  ],
  exports: [
    LanguageSelector,
    AppCardComponent,
    AppSpinner,
    AppDialogComponent,
    LinearDatePickerComponent,
  ],
  providers: [],
})
export class CommonsModule {}
