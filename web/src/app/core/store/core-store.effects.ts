import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { coreStoreActions } from './core-store.actions';
import { exhaustMap } from 'rxjs';

@Injectable()
export class CoreEffects {
  
  constructor(private store: Store, private actions$: Actions) {}
}
