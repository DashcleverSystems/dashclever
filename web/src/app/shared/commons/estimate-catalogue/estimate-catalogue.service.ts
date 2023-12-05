import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEstimatePdfDTO } from './estimate-form/estimate-form';

@Injectable()
export class EstimateCatalogueService {
  constructor(private http: HttpClient) {}

  getEstimateFromFile(file: File): Observable<IEstimatePdfDTO> {
    const data = new FormData();
    data.append('file', file);

    return this.http.post<IEstimatePdfDTO>('api/estimate/reader', data);
  }

  createSelectedEstimate(estimateId: string): Observable<any> {
    return this.http.post('api/planning', undefined, {
      params: new HttpParams().set('estimateId', estimateId),
    });
  }
}
