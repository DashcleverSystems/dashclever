import { NgModule } from '@angular/core';
import { LanguageSelector } from './language-selector/language-selector.component';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { PrimeModule } from '../prime.module';
import { AccessesModule } from '@shared/commons/accesses-selector/accesses-selector.module';
import { AccessesSelectorComponent } from '@shared/commons/accesses-selector/accesses-selector.component';
import { AppCardComponent } from './card/card.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppSpinner } from './spinner/spinner.component';
import { PaginatorModule } from 'primeng/paginator';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { HttpLoaderFactory } from '@core/translate/translate.module';
import { HttpClient } from '@angular/common/http';

@NgModule({
  declarations: [LanguageSelector, AppCardComponent],
  imports: [
    AccessesModule,
    CommonModule,
    PrimeModule,
    PaginatorModule,
    CalendarModule,
    FormsModule,
    ReactiveFormsModule,
    TranslateModule,
    AppSpinner,
    InputTextModule,
  ],
  exports: [
    LanguageSelector,
    AppCardComponent,
    AccessesSelectorComponent,
    CommonModule,
    PrimeModule,
    PaginatorModule,
    CalendarModule,
    FormsModule,
    ReactiveFormsModule,
    TranslateModule,
    AppSpinner,
    InputTextModule,
  ],
  providers: [],
})
export class CommonsModule {}
