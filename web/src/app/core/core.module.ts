import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { StoreModule } from '@ngrx/store';
import { coreStoreReducer } from './store/core-store.reducer';

@NgModule({
  imports: [SharedModule, StoreModule.forRoot({ core: coreStoreReducer })],
  exports: [],
  providers: [],
})
export class CoreModule {}
