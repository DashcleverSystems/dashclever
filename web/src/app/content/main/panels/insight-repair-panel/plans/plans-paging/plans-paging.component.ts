import {Component, OnDestroy, OnInit, SkipSelf} from '@angular/core';
import {Subject, takeUntil} from 'rxjs';
import {PlanDto, PlanFilters} from 'generated/openapi';
import {Table} from '@app/shared/services/table/table.service';
import {SortDirection} from '@shared/enums/sort-direction';
import {TableLazyLoadEvent} from 'primeng/table';
import {Router} from '@angular/router';
import {PlansPagingStore} from "@content/main/panels/insight-repair-panel/plans/plans-paging/plans-paging.store";
import {PlanCreatedNotifier} from "@content/main/panels/insight-repair-panel/plans/create-confirmation-dialog/plan-created-notifier.service";

@Component({
  selector: 'app-plans-paging',
  templateUrl: './plans-paging.component.html',
  styleUrls: ['./plans-paging.component.scss'],
  providers: [PlansPagingStore],
})
export class PlansPagingComponent
  extends Table<PlanDto, PlanFilters>
  implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  constructor(
    private readonly planningCreatedNotifier: PlanCreatedNotifier,
    tableStore: PlansPagingStore,
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

  setCreatedAfterFilter(date?: Date | null) {
  }

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
