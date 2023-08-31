import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { coreStoreReducer } from './store/core-store.reducer';
import { NgxTranslateModule } from './translate/translate.module';

@NgModule({
  imports: [
    SharedModule,
    StoreModule.forRoot({ core: coreStoreReducer }),
    EffectsModule.forRoot([]),
  ],
  exports: [],
  providers: [],
})
export class CoreModule {}
