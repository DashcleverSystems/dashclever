import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { PaginatorState } from 'primeng/paginator';
import { Store } from '@ngrx/store';
import { isMobile } from '@core/store/core-store.selectors';
import { Subject, distinctUntilChanged, takeUntil, Observable } from 'rxjs';
import { Estimate } from 'generated/openapi';
import { EstimateCatalogueService } from '../estimate-catalogue.service';
import { Table } from '../../../services/table.service';

@Component({
  selector: 'app-estimate-page',
  templateUrl: './estimate-page.component.html',
  styleUrls: ['./estimate-page.component.scss'],
})
export class EstimatePageComponent
  extends Table<Estimate>
  implements OnInit, OnDestroy
{
  @Input() refreshListener$: Observable<void> | undefined;
  isMobile: boolean = false;
  estimates: Estimate[] = [];

  private destroy$ = new Subject<void>();

  constructor(private store: Store, private service: EstimateCatalogueService) {
    super();
  }

  ngOnInit(): void {
    this.store
      .select(isMobile)
      .pipe(takeUntil(this.destroy$), distinctUntilChanged())
      .subscribe((mobile) => (this.isMobile = mobile));
    this.fetchPage();
    this.subscribeRefreshListener();
  }

  private subscribeRefreshListener(): void {
    if (this.refreshListener$) {
      this.refreshListener$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => this.fetchPage());
    }
  }

  paginate(event: PaginatorState): void {}

  private fetchPage(): void {
    this.service
      .getEstimateCatalogueList()
      .subscribe((res) => console.log(res));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
