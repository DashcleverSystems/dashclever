import {Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import { DialogService } from 'primeng/dynamicdialog';
import { EstimateFormComponent } from './estimate-form/estimate-form.component';
import { Store } from '@ngrx/store';
import { isMobile } from '@core/store/core-store.selectors';
import {Subject, distinctUntilChanged, finalize, takeUntil, Observable, of} from 'rxjs';
import { FileUpload } from 'primeng/fileupload';
import { EstimateCatalogueService } from './estimate-catalogue.service';
import { IEstimatePdfDTO } from './estimate-form/estimate-form';
import {Estimate, EstimateApiService} from "generated/openapi";

@Component({
  selector: 'app-estimate-catalogue',
  templateUrl: './estimate-catalogue.component.html',
  styleUrls: ['./estimate-catalogue.component.scss'],
  providers: [EstimateCatalogueService],
})
export class EstimateCatalogueComponent implements OnInit, OnDestroy {

  @ViewChild('fileUploader') fileUploader: FileUpload | undefined;
  private estimateCreated$: Subject<void> = new Subject<void>()

  get estimateCreatedListener() {
    return this.estimateCreated$.asObservable()
  }

  isMobile: boolean = false;
  loadingSpinner = false;

  estimates: Observable<Estimate[]> = of()

  page: Pageable | null = null

  private destroy$ = new Subject<void>();

  constructor(
    private dialogService: DialogService,
    private store: Store,
    private service: EstimateCatalogueService,
    private apiService: EstimateApiService
  ) {}

  ngOnInit(): void {
    this.store
      .select(isMobile)
      .pipe(takeUntil(this.destroy$), distinctUntilChanged())
      .subscribe((mobile) => (this.isMobile = mobile));
    this.apiService.get(undefined,)
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.estimateCreated$.next();
    this.estimateCreated$.complete();
  }

  createCatalogue(type: 'CREATE' | 'GENERATE', data?: any): void {
    if (type === 'CREATE') {
      this.openModalForm(type);
    }

    if (type === 'GENERATE') {
      this.loadingSpinner = true;
      this.service
        .getEstimateFromFile(data.currentFiles[0])
        .pipe(finalize(() => (this.loadingSpinner = false)))
        .subscribe((data) => {
          this.openModalForm('GENERATE', data);
        });

      if (this.fileUploader) {
        this.fileUploader.clear();
      }
    }
  }

  openModalForm(type: 'CREATE' | 'GENERATE', data?: IEstimatePdfDTO): void {
    this.dialogService.open(EstimateFormComponent, {
      data: {
        type,
        data: data,
      },
      showHeader: false,
      closable: false,
      width: this.isMobile ? '100svw' : undefined,
      style: {"min-width": !this.isMobile ? "40svw" : undefined },
      modal: true,
    }).onClose.subscribe(res =>
        res?.result === "success" && this.estimateCreated$.next()
    );
  }
}

interface Pageable {
  pageElements: number,
  pageNo: number,
  totalPages: number,
  totalElements: number
}
