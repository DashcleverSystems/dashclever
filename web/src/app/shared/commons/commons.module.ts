import { NgModule } from '@angular/core';
import { LanguageSelector } from './language-selector/language-selector.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '../prime.module';

@NgModule({
  declarations: [LanguageSelector],
  imports: [CommonModule, PrimeModule, TranslateModule],
  exports: [LanguageSelector],
  providers: [],
})
export class CommonsModule {}
