import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEstimatedPdfDTO } from './estimate-form/estimate-form';

@Injectable()
export class EstimateCatalogueService {
  constructor(private http: HttpClient) {}

  getEstimateFromFile(file: File): Observable<IEstimatedPdfDTO> {
    const data = new FormData();
    data.append('file', file);
    return this.http.post<IEstimatedPdfDTO>('/api/estimate/reader', data);
  }
}
