import { Component, Input, OnDestroy, OnInit, SkipSelf } from '@angular/core';
import { Store } from '@ngrx/store';
import { isMobile } from '@core/store/core-store.selectors';
import { distinctUntilChanged, Observable, Subject, takeUntil } from 'rxjs';
import { EstimateDto, EstimateFilters } from 'generated/openapi';
import { Table } from '@app/shared/services/table/table.service';
import { EstimatePageTableStore } from './estimate-page.store';
import { CreatePlanningConfirmationDialog } from '@app/content/main/panels/insight-repair-panel/planning/create-confirmation-dialog/create-planning.component';
import { ToastService } from '@app/shared/services/toast.service';
import { AppDialogService } from '@app/shared/commons/dialog/dialog.service';
import { TableLazyLoadEvent } from 'primeng/table';

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
    private toastr: ToastService,
    tableStore: EstimatePageTableStore,
    @SkipSelf() private dialog: AppDialogService,
  ) {
    super(tableStore);
    this.filters = {
      estimateId: null,
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
          this.toastr.success({
            message:
              'components.estimateCatalogueConfirmDialog.actions.success',
            translate: true,
          });
        }
      });
  }

  filter(event: TableLazyLoadEvent) {
    const createdAfterFilterKey = 'createdAfter';
    const customerNameFilterKey = 'customerName';
    const registrationFilterKey = 'registration';
    const brandFilterKey = 'brand';

    const eventCreatedAfterFilterValue =
      event.filters[createdAfterFilterKey][0]?.value ?? null;
    if (eventCreatedAfterFilterValue) {
      this.filters.createdAfter = new Date(
        eventCreatedAfterFilterValue,
      ).toISOString();
    } else {
      this.filters.createdAfter = null;
    }

    const eventCustomerNameFilterValue =
      event.filters[customerNameFilterKey][0]?.value ?? null;
    if (eventCustomerNameFilterValue) {
      this.filters.customerName = eventCustomerNameFilterValue;
    } else {
      this.filters.customerName = eventCustomerNameFilterValue;
    }

    const eventRegistrationFilterValue =
      event.filters[registrationFilterKey][0]?.value ?? null;
    if (eventRegistrationFilterValue) {
      this.filters.registration = eventRegistrationFilterValue;
    } else {
      this.filters.registration = null;
    }

    const eventBrandFilterValue =
      event.filters[brandFilterKey][0]?.value ?? null;
    if (eventBrandFilterValue) {
      this.filters.vehicleBrand = eventBrandFilterValue;
    } else {
      this.filters.vehicleBrand = null;
    }

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
