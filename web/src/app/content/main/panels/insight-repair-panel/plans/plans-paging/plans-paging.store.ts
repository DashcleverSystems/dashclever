import { Injectable } from '@angular/core';
import { TableStore } from '@app/shared/services/table/table.store';
import {
  PageRequestDto,
  PagingInfoPlanDto,
  PlanDto,
  PlanFilters,
  PlanningApiService,
} from 'generated/openapi';
import { switchMap, tap } from 'rxjs';

@Injectable()
export class PlansPagingStore extends TableStore<PlanDto> {
  constructor(private service: PlanningApiService) {
    super();
  }

  override getCollection = (filtersWithPageReq: PlanFilters & PageRequestDto) =>
    this.effect((effect) =>
      effect.pipe(
        switchMap(() =>
          this.service.filter(
            {
              createdAfter: filtersWithPageReq.createdAfter,
              estimateName: filtersWithPageReq.estimateName,
              sortDirection: filtersWithPageReq.sortDirection,
              hasRunningRepair: filtersWithPageReq.hasRunningRepair,
            },
            {
              pageNumber: filtersWithPageReq.pageNumber,
              pageSize: filtersWithPageReq.pageSize,
            },
          ),
        ),
        tap((data: PagingInfoPlanDto) => {
          this.setData(data?.content ?? []);
          this.setTotalElements(data.totalElements);
        }),
      ),
    );
}
