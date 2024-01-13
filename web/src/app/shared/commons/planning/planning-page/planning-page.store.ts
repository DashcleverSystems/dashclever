import { Injectable } from '@angular/core';
import { TableStore } from '@app/shared/services/table/table.store';
import {
  PagingInfoPlanDto,
  PlanDto,
  PlanFilters,
  PlanningApiService,
} from 'generated/openapi';
import { switchMap, tap } from 'rxjs';

@Injectable()
export class PlanningPageStore extends TableStore<PlanDto> {
  constructor(private service: PlanningApiService) {
    super();
  }

  override getCollection = (filters: PlanFilters) =>
    this.effect((effect) =>
      effect.pipe(
        switchMap(() => this.service.filter(filters)),
        tap((data: PagingInfoPlanDto) => {
          this.setData(data?.content ?? []);
          this.setTotalElements(data.totalElements);
        }),
      ),
    );
}
