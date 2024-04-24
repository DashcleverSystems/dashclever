import {
  Component,
  inject,
  Input,
  OnDestroy,
  OnInit,
  SkipSelf,
} from '@angular/core';
import { Observable, Subject, takeUntil } from 'rxjs';
import { EstimateDto, EstimateFilters } from 'generated/openapi';
import { Table } from '@app/shared/services/table/table.service';
import { EstimatePageTableStore } from './estimate-page.store';
import { CreatePlanningConfirmationDialog } from '@app/content/main/panels/insight-repair-panel/planning/create-confirmation-dialog/create-planning.component';
import { ToastService } from '@app/shared/services/toast.service';
import { AppDialogService } from '@app/shared/commons/dialog/dialog.service';
import { TableLazyLoadEvent } from 'primeng/table';
import { FiltersSetter } from '@shared/commons/primeng/filters-setter';
import CoreStore from '@app/core/store/core-store';

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

  get isMobile() {
    return this.coreStore.mobile();
  }

  private destroy$ = new Subject<void>();
  private coreStore = inject(CoreStore);

  constructor(
    private toast: ToastService,
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
    this.subscribeRefreshListener();
    this.getCollection();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  createPlanningFromEstimate(estimate: EstimateDto): void {
    this.dialog
      .open(CreatePlanningConfirmationDialog, {
        data: {
          estimate,
        },
        closable: false,
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

  private subscribeRefreshListener(): void {
    if (this.refreshContentListener$) {
      this.refreshContentListener$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => this.getCollection());
    }
  }
}
