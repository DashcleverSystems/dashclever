import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable()
export class EstimateCatalogueConfirmationCreateService {
  constructor(private readonly http: HttpClient) {}

  createPlan(estimateId: string): Observable<any> {
    return this.http.post('api/planning', undefined, {
      params: new HttpParams().set('estimateId', estimateId),
    });
  }
}
