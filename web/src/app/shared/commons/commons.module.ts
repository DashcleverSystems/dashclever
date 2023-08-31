import { NgModule } from '@angular/core';
import { LanguageSelector } from './language-selector/language-selector.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [LanguageSelector],
  imports: [CommonModule, TranslateModule],
  exports: [LanguageSelector],
})
export class CommonsModule {}
