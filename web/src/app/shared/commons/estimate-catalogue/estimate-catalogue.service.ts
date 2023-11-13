import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEstimatePdfDTO } from './estimate-form/estimate-form';
import { CacheService } from '@app/shared/services/cache.service';
import { Pageable } from '@app/shared/types/pageable';
import { Estimate } from 'generated/openapi';
@Injectable()
export class EstimateCatalogueService {
  constructor(private http: HttpClient, private cache: CacheService) {}

  getEstimateFromFile(file: File): Observable<IEstimatePdfDTO> {
    const data = new FormData();
    data.append('file', file);

    return this.http.post<IEstimatePdfDTO>('api/estimate/reader', data);
  }

  getEstimateCatalogueList(): Observable<Pageable<Estimate>> {
    return this.http.get<Pageable<Estimate>>('api/estimatecatalogue');
  }
}
