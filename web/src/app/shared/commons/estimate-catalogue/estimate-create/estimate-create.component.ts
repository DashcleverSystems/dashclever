import { Component, ViewChild } from '@angular/core';
import { distinctUntilChanged, finalize, Subject, takeUntil } from 'rxjs';
import { DialogService } from 'primeng/dynamicdialog';
import { Store } from '@ngrx/store';
import { IEstimatePdfDTO } from '@shared/commons/estimate-catalogue/estimate-form/estimate-form';
import { EstimateFormComponent } from '@shared/commons/estimate-catalogue/estimate-form/estimate-form.component';
import { FileUpload } from 'primeng/fileupload';
import { isMobile } from '@core/store/core-store.selectors';
import { EstimateCreateService } from '@shared/commons/estimate-catalogue/estimate-create/estimate-create.service';

@Component({
  selector: 'app-estimate-create',
  templateUrl: './estimate-create.component.html',
  styleUrl: './estimate-create.component.css',
  providers: [EstimateCreateService],
})
export class EstimateCreateComponent {
  @ViewChild('fileUploader') fileUploader: FileUpload | undefined;

  isMobile: boolean = false;
  loadingSpinner = false;

  private estimateCreated$: Subject<void> = new Subject<void>();
  private destroy$ = new Subject<void>();

  constructor(
    private dialogService: DialogService,
    private store: Store,
    private service: EstimateCreateService,
  ) {}

  ngOnInit(): void {
    this.store
      .select(isMobile)
      .pipe(takeUntil(this.destroy$), distinctUntilChanged())
      .subscribe((mobile) => (this.isMobile = mobile));
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
    this.dialogService
      .open(EstimateFormComponent, {
        data: {
          type,
          data: data,
        },
        showHeader: false,
        closable: false,
        width: this.isMobile ? '100svw' : undefined,
        style: { 'min-width': !this.isMobile ? '40svw' : undefined },
        modal: true,
      })
      .onClose.subscribe(
        (res) => res?.result === 'success' && this.estimateCreated$.next(),
      );
  }
}
