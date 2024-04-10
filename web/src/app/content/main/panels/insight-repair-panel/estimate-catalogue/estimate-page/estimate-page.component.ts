import { Component, Input, OnDestroy, OnInit, SkipSelf } from '@angular/core';
import { Store } from '@ngrx/store';
import { isMobile } from '@core/store/core-store.selectors';
import { distinctUntilChanged, Observable, Subject, takeUntil } from 'rxjs';
import { EstimateDto, EstimateFilters } from 'generated/openapi';
import { Table } from '@app/shared/services/table/table.service';
import { EstimatePageTableStore } from './estimate-page.store';
import { CreatePlanningConfirmationDialog } from '@app/content/main/panels/insight-repair-panel/planning/create-confirmation-dialog/create-planning.component';
import { ToastService } from '@app/shared/services/toast.service';
import { EstimateCreateNotifier } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-create/estimate-create.notifier';
import { AppDialogService } from '@app/shared/commons/dialog/dialog.service';
import { TableLazyLoadEvent } from 'primeng/table';
import { FiltersSetter } from '@shared/commons/primeng/filters-setter';

@Component({
  selector: 'app-estimate-page',
  templateUrl: './estimate-page.component.html',
  styleUrls: ['./estimate-page.component.scss'],
  providers: [EstimatePageTableStore],
})
export class EstimatePageComponent
  extends Table<EstimateDto, EstimateFilters>
  implements OnInit, OnDestroy
{
  @Input() refreshContentListener$: Observable<void> | undefined;
  isMobile: boolean = false;

  private destroy$ = new Subject<void>();

  constructor(
    private store: Store,
    private toast: ToastService,
    private notifier: EstimateCreateNotifier,
    tableStore: EstimatePageTableStore,
    @SkipSelf() private dialog: AppDialogService,
  ) {
    super(tableStore);
    this.filters = {
      estimateName: null,
      createdAfter: null,
    };
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
    if (this.refreshContentListener$) {
      this.refreshContentListener$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => this.getCollection());
    }
  }

  setCreatedAfterFilter(date?: Date | null) {
    if (date != null) {
      this.filters.createdAfter = date.toISOString();
    } else {
      this.filters.createdAfter = null;
    }
  }

  checkEmpty(newValue: string | null) {
    if (newValue != null && newValue.length == 0) {
      this.filters.estimateName = null;
    }
  }

  createPlanningFromEstimate(estimate: EstimateDto): void {
    this.dialog
      .open(CreatePlanningConfirmationDialog, {
        data: {
          estimate,
        },
        closable: false,
        width: this.isMobile ? '100svw' : undefined,
        style: { 'min-width': !this.isMobile ? '40svw' : undefined },
        // modal: true,
      })
      .onClose.subscribe((res) => {
        if (res) {
          this.toast.success({
            message:
              'components.estimateCatalogueConfirmDialog.actions.success',
            translate: true,
          });
        }
      });
  }

  filter(event: TableLazyLoadEvent) {
    const filtersSetter = new FiltersSetter(this.filters, event.filters);

    filtersSetter.setFilter(
      (filters: EstimateFilters, primeFilterValue: string) =>
        (filters.customerName = primeFilterValue),
      'customerName',
    );

    filtersSetter.setFilter(
      (filters: EstimateFilters, primeFilterValue: string) =>
        (filters.vehicleBrand = primeFilterValue),
      'brand',
    );

    filtersSetter.setFilter(
      (filters: EstimateFilters, primeFilterValue: string) =>
        (filters.registration = primeFilterValue),
      'registration',
    );

    filtersSetter.setFilter(
      (filters: EstimateFilters, primeFilterValue: string) =>
        (filters.createdAfter = primeFilterValue),
      'createdAfter',
      (filterMetadata) => {
        if (!filterMetadata) return null;
        const filterValue = filterMetadata[0]?.value ?? null;
        if (filterValue) return new Date(filterValue).toISOString();
        else return null;
      },
    );

    this.getCollection();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private subscribeRefreshListener(): void {
    if (this.refreshContentListener$) {
      this.refreshContentListener$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => this.getCollection());
    }
  }
}
