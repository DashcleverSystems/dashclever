import { NgModule } from '@angular/core';

import { ButtonModule } from 'primeng/button';
import { SplitButtonModule } from 'primeng/splitbutton';
import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';

@NgModule({
  imports: [ButtonModule, SplitButtonModule, TooltipModule, ToastModule],
  exports: [ButtonModule, SplitButtonModule, TooltipModule, ToastModule],
})
export class PrimeModule {}
