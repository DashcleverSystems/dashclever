import { Component, OnDestroy, OnInit, SkipSelf } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { PlanDto, PlanFilters } from 'generated/openapi';
import { Table } from '@app/shared/services/table/table.service';
import { PlanningPageStore } from '@app/content/main/panels/insight-repair-panel/planning/planning-page/planning-page.store';
import { PlanningCreatedNotifier } from '@app/content/main/panels/insight-repair-panel/planning/create-confirmation-dialog/planning-created.notifier';
import { SortDirection } from '@shared/enums/sort-direction';
import { TableLazyLoadEvent } from 'primeng/table';
import { Router } from '@angular/router';

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

  constructor(
    private readonly planningCreatedNotifier: PlanningCreatedNotifier,
    tableStore: PlanningPageStore,
    @SkipSelf() private router: Router,
  ) {
    super(tableStore);
    this.filters = {
      estimateName: undefined,
      createdAfter: undefined,
      sortDirection: undefined,
    };
  }

  ngOnInit(): void {
    this.getCollection();
    this.planningCreatedNotifier.planningCreated$
      .asObservable()
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.getCollection());
  }

  setCreatedAfterFilter(date?: Date | null) {}

  checkEmpty(value?: string | null) {
    if (value == null || value.length <= 0) {
      this.filters.estimateName = null;
    }
  }

  setSort(loadEvent: TableLazyLoadEvent) {
    const direction = this.determineSortDirection(loadEvent.sortOrder);
    this.filters = {
      ...this.filters,
      sortDirection: direction,
    };
    this.getCollection();
  }

  startPlanning(planningId: string): void {
    this.router.navigate(['insight-repair/planning', planningId, 'plan']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private determineSortDirection(sortOrder?: number): SortDirection | null {
    switch (sortOrder) {
      case -1:
        return SortDirection.DESC;
      case 1:
        return SortDirection.ASC;
      default:
        return null;
    }
  }
}
