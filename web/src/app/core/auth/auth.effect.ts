import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { AuthService } from './component/auth.service';
import { coreStoreActions } from '../store/core-store.actions';
import { catchError, exhaustMap, map, of } from 'rxjs';

@Injectable()
export class AuthEffects {
  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(coreStoreActions.login),
      exhaustMap((action) =>
        this.service.login(action.credentials).pipe(
          map((user) =>
            coreStoreActions.loginSuccessfully({ credentials: user })
          ),
          catchError((err) => of(coreStoreActions.loginFail()))
        )
      )
    )
  );
  constructor(private actions$: Actions, private service: AuthService) {}
}

