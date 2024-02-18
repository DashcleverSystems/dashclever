import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEstimatePdfDTO } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-form';

@Injectable()
export class EstimateCreateService {
  constructor(private http: HttpClient) {}

  getEstimateFromFile(file: File): Observable<IEstimatePdfDTO> {
    const data = new FormData();
    data.append('file', file);

    return this.http.post<IEstimatePdfDTO>('api/estimate/reader', data);
  }
}
