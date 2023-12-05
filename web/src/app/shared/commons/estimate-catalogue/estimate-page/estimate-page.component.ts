import { Component, Input, OnDestroy, OnInit, SkipSelf } from '@angular/core';
import { Store } from '@ngrx/store';
import { isMobile } from '@core/store/core-store.selectors';
import { Subject, distinctUntilChanged, takeUntil, Observable } from 'rxjs';
import { EstimateDto, EstimateFilters } from 'generated/openapi';
import { Table } from '@app/shared/services/table/table.service';
import { EstimatePageTableStore } from './estimate-page.store';
import { DialogService } from 'primeng/dynamicdialog';
import { EstimateCatalogueConfirmationCreateComponent } from '../estimate-catalogue-confirmation-create/estimate-catalogue-confirmation-create.component';
import { ToastService } from '@app/shared/services/toast.service';

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
    @SkipSelf() private dialogService: DialogService,
    private toastr: ToastService,
    tableStore: EstimatePageTableStore,
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

  private subscribeRefreshListener(): void {
    if (this.refreshContentListener$) {
      this.refreshContentListener$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => this.getCollection());
    }
  }

  setCreatedAfterFilter(date: Date | null) {
    if (date != null) {
      this.filters.createdAfter = date.toISOString();
    } else {
      this.filters.createdAfter = null;
    }
  }

  checkEmpty(newValue: string | null) {
    if (newValue != null && newValue.length == 0) {
      this.filters.estimateId = null;
    }
  }

  createEstimate(estimate: EstimateDto): void {
    this.dialogService
      .open(EstimateCatalogueConfirmationCreateComponent, {
        data: {
          estimate,
        },
        showHeader: false,
        closable: false,
        width: this.isMobile ? '100svw' : undefined,
        style: { 'min-width': !this.isMobile ? '40svw' : undefined },
        modal: true,
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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
