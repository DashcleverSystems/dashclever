import { Injectable } from '@angular/core';
import { TableStore } from '@app/shared/services/table/table.store';
import {
  EstimateApiService,
  EstimateDto,
  EstimateFilters,
  PageEstimateDto,
} from 'generated/openapi';
import { switchMap, tap } from 'rxjs';

@Injectable()
export class EstimatePageTableStore extends TableStore<EstimateDto> {
  constructor(private service: EstimateApiService) {
    super();
  }

  override getCollection = (filters: EstimateFilters) =>
    this.effect((effect) =>
      effect.pipe(
        switchMap(() => this.service.get(filters)),
        tap((data: PageEstimateDto) => {
          this.setData(data.content);
          this.setTotalElements(data.totalElements);
        }),
      ),
    );
}
