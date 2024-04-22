import {Component, OnInit} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {IEstimateReportDto, IEstimateReportForm} from "./estimate-report-form";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {ToastService} from "@shared/services/toast.service";
import {
  EstimateReportFormService
} from "@content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-report-form/estimate-report-form.service";
import {catchError, EMPTY, finalize} from "rxjs";
import {
  EstimateFormComponent
} from "@content/main/panels/insight-repair-panel/estimate-catalogue/estimate-form/estimate-form.component";
import {AppDialogService} from "@shared/commons/dialog/dialog.service";

@Component({
  templateUrl: './estimate-report-form.component.html',
  providers: [EstimateReportFormService]
})
export class EstimateReportFormComponent implements OnInit {
  form: FormGroup<IEstimateReportForm> = this.service.createForm();

  loadingSpinner = false;

  constructor(
    private service: EstimateReportFormService,
    private conf: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private dialog: AppDialogService,
    private toast: ToastService,
  ) {}

  ngOnInit(): void {
  }

  submitForm(): void {
    this.loadingSpinner = true;
    if (this.form.invalid) {
      this.toast.error({
        title: 'error.errorOccurred',
        message: 'error.form.validationError',
        translate: true,
      });
      this.loadingSpinner = false;
      this.form.markAllAsTouched();
      return;
    }

    this.service
      .save(this.form.getRawValue() as IEstimateReportDto)
      .pipe(
        catchError((err) => {
          this.toast.error({
            title: 'components.estimateCatalogue.form.toast.titleSave',
            message: err.error.message,
            translate: true,
          });

          return EMPTY;
        }),
        finalize(() => (this.loadingSpinner = false)),
      )
      .subscribe(() => {
        this.toast.success({
          title: 'components.estimateCatalogue.form.toast.titleSave',
          message: 'components.estimateCatalogue.form.toast.successSave',
          translate: true,
        });
        this.ref.close({ result: 'success' });
      });
  }

  createEstimateReport(): void {
    this.openModalForm();
  }

  private openModalForm(data?: IEstimateReportDto): void {
    this.dialog
      .open(EstimateFormComponent, {
        data: {
          data: data,
        },
        showHeader: false,
        closable: false,
        width: undefined,
        style: { 'min-width': undefined },
        modal: true,
      }
    );
  }
}
