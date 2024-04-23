import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { CoreService } from './store/core-store.service';

@NgModule({
  imports: [SharedModule],
  exports: [],
  providers: [CoreService],
})
export class CoreModule {}
