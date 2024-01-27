import { NgModule } from '@angular/core';
import { PrimeModule } from './prime.module';
import { CommonsModule } from './commons/commons.module';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    PrimeModule,
    CommonModule,
    CommonsModule,
    TranslateModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  exports: [
    PrimeModule,
    CommonModule,
    CommonsModule,
    TranslateModule,
    FormsModule,
    ReactiveFormsModule,
  ],
})
export class SharedModule {}
