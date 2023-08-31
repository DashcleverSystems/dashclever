import { NgModule } from '@angular/core';

import { ButtonModule } from 'primeng/button';
import { SplitButtonModule } from 'primeng/splitbutton';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
  imports: [ButtonModule, SplitButtonModule, TooltipModule],
  exports: [ButtonModule, SplitButtonModule, TooltipModule],
})
export class PrimeModule {}
