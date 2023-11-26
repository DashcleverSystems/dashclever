import { Injectable, inject } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { Store } from '@ngrx/store';
import {
  Observable,
  catchError,
  of,
  switchMap,
  tap,
  combineLatest,
  map,
} from 'rxjs';
import {
  currentPermissions,
  isAuthorized,
} from '../store/core-store.selectors';
import { ToastService } from '@app/shared/services/toast.service';
import { HttpClient } from '@angular/common/http';
import { coreStoreActions } from '../store/core-store.actions';
import { IWorkshop } from '@app/shared/models/workshop';
import { AccessDto } from '@api/models/accessDto';
import { AccountRestApiService } from '@api/services/accountRestApi.service';

@Injectable({
  providedIn: 'root',
})
export class PermissionService {
  constructor(
    private router: Router,
    private store: Store,
    private accountApi: AccountRestApiService,
    private toast: ToastService,
    private http: HttpClient,
  ) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): boolean | Observable<boolean> {
    const permissions = next.data['permissions'];

    if (Array.isArray(permissions) && permissions.length > 0) {
      return this.checkPermissions(permissions).pipe(
        switchMap((permitted) => (!permitted ? this.notPermitted() : of(true))),
      );
    } else if (Array.isArray(permissions)) {
      return true;
    }

    return this.notPermitted();
  }

  isAuthorized(): Observable<boolean> {
    return this.store.select(isAuthorized).pipe(
      switchMap((isAuth) => {
        if (!isAuth) {
          return this.isLogged().pipe(
            switchMap(() => this.getPermissions()),
            tap((workshops) => {
              this.store.dispatch(
                coreStoreActions.loginSuccessfully({ logged: true }),
              );
              this.store.dispatch(
                coreStoreActions.changeWorkshops({ workshops }),
              );
            }),
            switchMap(() => of(true)),
            catchError(() => this.notAuthorized()),
          );
        }

        return of(isAuth);
      }),
    );
  }

  hasPermission(permission: string): Observable<boolean> {
    return this.store.select(currentPermissions).pipe(
      switchMap((avPermissions) => {
        return of(!!avPermissions.find((p) => p === permission));
      }),
    );
  }

  hasAnyPermission(permissions: string[]): Observable<boolean> {
    return this.store
      .select(currentPermissions)
      .pipe(
        switchMap((avPermissions) =>
          of(permissions.some((p) => avPermissions.includes(p))),
        ),
      );
  }

  getPermissions() {
    return this.http.get<IWorkshop[]>('api/account/access');
  }

  private checkPermissions(permissions: string[]): Observable<boolean> {
    const permissionsFromStore$ = this.store.select(currentPermissions);
    const permissionsFromApi$ = this.accountApi.currentUser().pipe(
      map((accessDto: AccessDto) => Array.from(accessDto.authorities)),
      map((authorities) => authorities.map((auth) => auth.toString())),
    );
    return combineLatest([permissionsFromStore$, permissionsFromApi$]).pipe(
      switchMap(([store, api]) => {
        console.log(store);
        console.log(api);
        return of(
          permissions.every(
            (perm) => store.includes(perm) || api.includes(perm),
          ),
        );
      }),
    );
  }

  private notPermitted(): Observable<false> {
    const currentUrl = this.router.url.split('/').slice(1).join('/');

    this.toast.error({
      title: 'toast.router.permissions.title',
      message: 'toast.router.permissions.message',
      translate: true,
    });

    this.router.navigate([currentUrl]);

    return of(false);
  }

  private notAuthorized(): Observable<false> {
    this.toast.error({
      title: 'toast.unauthorized.title',
      message: 'toast.unauthorized.message',
      translate: true,
    });

    this.router.navigate(['login']);

    return of(false);
  }

  private isLogged(): Observable<any> {
    return this.http.get('/api/account');
  }
}

export const PermissionGuard: CanActivateFn = (
  next: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
): boolean | Observable<boolean> =>
  inject(PermissionService).canActivate(next, state);

export const AuthorizedGuard: CanActivateFn = (
  next: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
): boolean | Observable<boolean> => inject(PermissionService).isAuthorized();
