import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEstimatePdfDTO } from './estimate-form/estimate-form';
import { HttpService } from '@app/shared/services/http.service';
import { CacheService } from '@app/shared/services/cache.service';
import { Pageable } from '@app/shared/types/pageable';
import { Estimate } from 'generated/openapi';

enum EndpointTypes {
  SEND_ESTIMATE_CATALOGUE_PDF = 'SEND_ESTIMATE_CATALOGUE_PDF',
  GET_ESTIMATE_CATALOGUE_LIST = 'GET_ESTIMATE_CATALOGUE_LIST',
}
@Injectable()
export class EstimateCatalogueService extends HttpService<EndpointTypes> {
  constructor(http: HttpClient, cache: CacheService) {
    super(http, cache);
  }

  getEstimateFromFile(file: File): Observable<IEstimatePdfDTO> {
    const data = new FormData();
    data.append('file', file);

    return this.post<IEstimatePdfDTO>('api/estimate/reader', data, {
      type: EndpointTypes.SEND_ESTIMATE_CATALOGUE_PDF,
    });
  }

  getEstimateCatalogueList(): Observable<Pageable<Estimate>> {
    return this.getCollection<Estimate>('api/estimatecatalogue', {
      type: EndpointTypes.GET_ESTIMATE_CATALOGUE_LIST,
    });
  }
}
