import { Injectable } from '@angular/core';
import { TableStore } from '@app/shared/services/table/table.store';
import {
  Estimate,
  EstimateApiService,
  EstimateFilters,
  PageEstimate,
} from 'generated/openapi';
import { switchMap, tap } from 'rxjs';

@Injectable()
export class EstimatePageTableStore extends TableStore<Estimate> {
  constructor(private service: EstimateApiService) {
    super();
  }

  override getCollection = (filters: EstimateFilters) =>
    this.effect((effect) =>
      effect.pipe(
        switchMap(() => this.service.get(filters)),
        tap((data: PageEstimate) => {
          this.setData(data.content);
          this.setTotalElements(data.totalElements);
        })
      )
    );
}
