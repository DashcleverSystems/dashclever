import { inject, Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { catchError, map, Observable, of, switchMap } from 'rxjs';
import { ToastService } from '@app/shared/services/toast.service';
import { AccessDto } from '@api/models/accessDto';
import { AccountRestApiService } from '@api/services/accountRestApi.service';

@Injectable({
  providedIn: 'root',
})
export class PermissionService {
  constructor(
    private router: Router,
    private accountApi: AccountRestApiService,
    private toast: ToastService,
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
    return this.accountApi.currentUser().pipe(
      map(() => true),
      catchError(() => of(false)),
    );
  }

  private checkPermissions(permissions: string[]): Observable<boolean> {
    const permissionsFromApi$: Observable<string[]> = this.accountApi
      .currentUser()
      .pipe(map((accessDto: AccessDto) => Array.from(accessDto.authorities)));
    return permissionsFromApi$.pipe(
      switchMap((permissionsFromApi: string[]) => {
        return of(
          permissions.every((permission: string) =>
            permissionsFromApi.includes(permission),
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
