import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { distinctUntilChanged, finalize, Subject, takeUntil } from 'rxjs';
import { Store } from '@ngrx/store';
import { IEstimatePdfDTO } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-form';
import { EstimateFormComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-form.component';
import { FileUpload } from 'primeng/fileupload';
import { isMobile } from '@core/store/core-store.selectors';
import { EstimateCreateService } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-create/estimate-create.service';
import { EstimateCreateNotifier } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-create/estimate-create.notifier';
import { AppDialogService } from '@app/shared/commons/dialog/dialog.service';

@Component({
  selector: 'app-estimate-create',
  templateUrl: './estimate-create.component.html',
  styleUrl: './estimate-create.component.css',
  providers: [EstimateCreateService],
})
export class EstimateCreateComponent implements OnInit, OnDestroy {
  @ViewChild('fileUploader') fileUploader: FileUpload | undefined;

  isMobile: boolean = false;
  loadingSpinner = false;

  private destroy$ = new Subject<void>();

  constructor(
    private store: Store,
    private service: EstimateCreateService,
    private notifier: EstimateCreateNotifier,
    private dialog: AppDialogService,
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
    this.notifier.estimateCreated$.next();
    this.notifier.estimateCreated$.complete();
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
    this.dialog
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
        (res) =>
          res?.result === 'success' && this.notifier.estimateCreated$.next(),
      );
  }
}
