import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastService } from '@app/shared/services/toast.service';
import { catchError, Observable } from 'rxjs';
import { environment } from 'src/environments/environments';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class ErrorHandlerInterceptor implements HttpInterceptor {
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    return next
      .handle(request)
      .pipe(catchError((error) => this.errorHandler(error)));
  }

  private errorHandler(
    response: HttpEvent<any> | any,
  ): Observable<HttpEvent<any>> {
    if (!environment.production) {
      console.error('Request error', response);
    }
    if (response?.status === 401) {
      this.router.navigate(['login']).finally();
    }

    if (response?.status === 403) {
      this.toast.error({
        title: 'toast.unauthorized.title',
        message: 'toast.unauthorized.message',
        translate: true,
      });
    }

    throw response;
  }

  constructor(
    private readonly router: Router,
    private toast: ToastService,
  ) {}
}
