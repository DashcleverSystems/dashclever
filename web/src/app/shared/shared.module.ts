import { NgModule } from '@angular/core';
import { CommonsModule } from './commons/commons.module';

@NgModule({
  imports: [CommonsModule],
  exports: [CommonsModule],
})
export class SharedModule {}
