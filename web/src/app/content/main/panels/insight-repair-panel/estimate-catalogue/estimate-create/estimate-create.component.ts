import { Component, inject, OnDestroy, ViewChild } from '@angular/core';
import { finalize, Subject } from 'rxjs';
import { IEstimatePdfDTO } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-form';
import { EstimateFormComponent } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-form.component';
import { FileUpload } from 'primeng/fileupload';
import { EstimateCreateService } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-create/estimate-create.service';
import { EstimateCreateNotifier } from '@app/content/main/panels/insight-repair-panel/estimate-catalogue/estimate-create/estimate-create.notifier';
import { AppDialogService } from '@app/shared/commons/dialog/dialog.service';
import CoreStore from '@app/core/store/core-store';

@Component({
  selector: 'app-estimate-create',
  templateUrl: './estimate-create.component.html',
  styleUrl: './estimate-create.component.scss',
  providers: [EstimateCreateService],
})
export class EstimateCreateComponent implements OnDestroy {
  @ViewChild('fileUploader') fileUploader: FileUpload | undefined;

  loadingSpinner = false;

  get isMobile() {
    return this.coreStore.mobile();
  }

  private destroy$ = new Subject<void>();
  private coreStore = inject(CoreStore);

  constructor(
    private service: EstimateCreateService,
    private notifier: EstimateCreateNotifier,
    private dialog: AppDialogService,
  ) {}

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.notifier.estimateCreated$.next();
    this.notifier.estimateCreated$.complete();
  }

  createEstimate(type: 'CREATE' | 'GENERATE', data?: any): void {
    if (type === 'CREATE') {
      this.openModalForm();
    }

    if (type === 'GENERATE') {
      this.loadingSpinner = true;
      this.service
        .getEstimateFromFile(data.currentFiles[0])
        .pipe(finalize(() => (this.loadingSpinner = false)))
        .subscribe((data) => {
          this.openModalForm(data);
        });

      if (this.fileUploader) {
        this.fileUploader.clear();
      }
    }
  }

  private openModalForm(data?: IEstimatePdfDTO): void {
    this.dialog
      .open(EstimateFormComponent, {
        data: { data },
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
