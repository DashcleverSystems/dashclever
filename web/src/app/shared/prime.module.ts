import { NgModule } from '@angular/core';

import { ButtonModule } from 'primeng/button';
import { SplitButtonModule } from 'primeng/splitbutton';
import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';

@NgModule({
  imports: [
    ButtonModule,
    SplitButtonModule,
    TooltipModule,
    ToastModule,
    InputTextModule,
  ],
  exports: [
    ButtonModule,
    SplitButtonModule,
    TooltipModule,
    ToastModule,
    InputTextModule,
  ],
})
export class PrimeModule {}
