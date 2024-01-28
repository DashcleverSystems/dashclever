import { NgModule } from '@angular/core';
import { LanguageSelector } from './language-selector/language-selector.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '../prime.module';
import { AppCardComponent } from './card/card.component';
import { AppSpinner } from './spinner/spinner.component';
import { AppDialogComponent } from './dialog/dialog.component';

@NgModule({
  declarations: [LanguageSelector, AppDialogComponent],
  imports: [
    CommonModule,
    PrimeModule,
    TranslateModule,
    AppCardComponent,
    AppSpinner,
  ],
  exports: [LanguageSelector, AppCardComponent, AppSpinner, AppDialogComponent],
  providers: [],
})
export class CommonsModule {}
