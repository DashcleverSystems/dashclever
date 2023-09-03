import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastService } from '@app/shared/services/toast.service';
import { Store } from '@ngrx/store';
import { Observable, catchError } from 'rxjs';
import { environment } from 'src/environments/environments';
import { coreStoreActions } from '../store/core-store.actions';
@Injectable({
  providedIn: 'root',
})
export class ErrorHandlerInterceptor implements HttpInterceptor {
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next
      .handle(request)
      .pipe(catchError((error) => this.errorHandler(error)));
  }

  private errorHandler(
    response: HttpEvent<any> | any
  ): Observable<HttpEvent<any>> {
    if (!environment.production) {
      console.error('Request error', response);
    }

    if (response?.status === 401) {
      this.unAuthorized();
    }

    throw response;
  }

  private unAuthorized(): void {
    this.toast.error({
      title: 'toast.unauthorized.title',
      message: 'toast.unauthorized.message',
      translate: true,
    });
    this.store.dispatch(coreStoreActions.unauthorized());
  }

  constructor(private store: Store, private toast: ToastService) {}
}
