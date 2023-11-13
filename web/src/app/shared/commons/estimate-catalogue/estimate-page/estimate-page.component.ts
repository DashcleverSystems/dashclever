import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { isMobile } from '@core/store/core-store.selectors';
import { Subject, distinctUntilChanged, takeUntil, Observable } from 'rxjs';
import {
  Estimate,
  EstimateFilters,
} from 'generated/openapi';
import { Table } from '@app/shared/services/table/table.service';
import { EstimatePageTableStore } from './estimate-page.store';

@Component({
  selector: 'app-estimate-page',
  templateUrl: './estimate-page.component.html',
  styleUrls: ['./estimate-page.component.scss'],
  providers: [EstimatePageTableStore],
})
export class EstimatePageComponent
  extends Table<Estimate, EstimateFilters>
  implements OnInit, OnDestroy
{
  @Input() refreshListener$: Observable<void> | undefined;
  isMobile: boolean = false;

  private destroy$ = new Subject<void>();

  constructor(private store: Store, tableStore: EstimatePageTableStore) {
    super(tableStore);
  }

  ngOnInit(): void {
    this.store
      .select(isMobile)
      .pipe(takeUntil(this.destroy$), distinctUntilChanged())
      .subscribe((mobile) => (this.isMobile = mobile));
    this.subscribeRefreshListener();
    this.getCollection();
  }

  private subscribeRefreshListener(): void {
    if (this.refreshListener$) {
      this.refreshListener$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => this.getCollection());
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
