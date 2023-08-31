import { NgModule } from '@angular/core';
import { PrimeModule } from './prime.module';
import { CommonsModule } from './commons/commons.module';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [PrimeModule, CommonModule, CommonsModule],
  exports: [PrimeModule, CommonModule, CommonsModule],
})
export class SharedModule {}
