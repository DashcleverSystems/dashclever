import { Injectable, inject } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable, of, switchMap } from 'rxjs';
import { availablePermissions } from '../store/core-store.selectors';
import { ToastService } from '@app/shared/services/toast.service';

@Injectable({
  providedIn: 'root',
})
export class PermissionService {
  constructor(
    private router: Router,
    private store: Store,
    private toast: ToastService
  ) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | Observable<boolean> {
    const permissions = next.data['permissions'];

    if (Array.isArray(permissions) && permissions.length > 0) {
      return this.checkPermissions(permissions).pipe(
        switchMap((permitted) => (!permitted ? this.notPermitted() : of(true)))
      );
    } else if (Array.isArray(permissions)) {
      return true;
    }

    return this.notPermitted();
  }

  hasPermission(permission: string): Observable<boolean> {
    return this.store.select(availablePermissions).pipe(
      switchMap((avPermissions) => {
        return of(!!avPermissions.find((p) => p === permission));
      })
    );
  }

  hasAnyPermission(permissions: string[]): Observable<boolean> {
    return this.store
      .select(availablePermissions)
      .pipe(
        switchMap((avPermissions) =>
          of(permissions.some((p) => avPermissions.includes(p)))
        )
      );
  }

  private checkPermissions(permissions: string[]): Observable<boolean> {
    return this.store
      .select(availablePermissions)
      .pipe(
        switchMap((avPermissions) =>
          of(permissions.every((perm) => avPermissions.includes(perm)))
        )
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

export const AuthGuard: CanActivateFn = (
  next: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
): boolean | Observable<boolean> =>
  inject(PermissionService).canActivate(next, state);
