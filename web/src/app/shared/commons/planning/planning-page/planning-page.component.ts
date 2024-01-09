import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { PlanDto, PlanFilters } from 'generated/openapi';
import { Table } from '@app/shared/services/table/table.service';
import { PlanningPageStore } from '@shared/commons/planning/planning-page/planning-page.store';

@Component({
  selector: 'app-planning-page',
  templateUrl: './planning-page.component.html',
  styleUrls: ['./planning-page.component.scss'],
  providers: [PlanningPageStore],
})
export class PlanningPageComponent
  extends Table<PlanDto, PlanFilters>
  implements OnInit, OnDestroy
{
  private destroy$ = new Subject<void>();

  constructor(tableStore: PlanningPageStore) {
    super(tableStore);
    this.filters = {
      estimateId: null,
      createdAfter: null,
      estimateName: null,
      sort: null,
    };
  }

  ngOnInit(): void {
    this.getCollection();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
