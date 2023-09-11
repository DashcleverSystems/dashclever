import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { AuthService } from './component/auth.service';
import { coreStoreActions } from '../store/core-store.actions';
import {
  EMPTY,
  catchError,
  exhaustMap,
  finalize,
  map,
  of,
  switchMap,
  tap,
} from 'rxjs';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { isAuthorized } from '../store/core-store.selectors';
import { PermissionService } from './permission.service';

@Injectable()
export class AuthEffects {
  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(coreStoreActions.login),
      exhaustMap((action) =>
        this.service.login(action.credentials).pipe(
          map((user) => {
            this.router.navigate(['home']);
            return coreStoreActions.loginSuccessfully({ logged: true });
          }),
          catchError((err) => of(coreStoreActions.loginFail()))
        )
      )
    )
  );

  loginSuccessfully$ = createEffect(() =>
    this.actions$.pipe(
      ofType(coreStoreActions.loginSuccessfully),
      exhaustMap((logged) =>
        this.permissionService
          .getPermissions()
          .pipe(
            map((workshops) => coreStoreActions.changeWorkshops({ workshops }))
          )
      )
    )
  );

  logout$ = createEffect(() =>
    this.actions$.pipe(
      ofType(coreStoreActions.logout),
      exhaustMap(() =>
        this.service.logout().pipe(
          map(
            () => coreStoreActions.logoutSuccessfully(),
            catchError(() => of(coreStoreActions.logoutSuccessfully))
          ),
          finalize(() => {
            this.store.dispatch(coreStoreActions.clearCoreState());
            this.router.navigate(['login']);
          })
        )
      )
    )
  );

  unauthorized$ = createEffect(() =>
    this.actions$.pipe(
      ofType(coreStoreActions.unauthorized),
      switchMap(() => this.store.select(isAuthorized)),
      map((isAuthorized) => {
        if (isAuthorized) return coreStoreActions.logout();

        return coreStoreActions.loginFail();
      })
    )
  );

  constructor(
    private actions$: Actions,
    private service: AuthService,
    private permissionService: PermissionService,
    private router: Router,
    private store: Store
  ) {}
}
