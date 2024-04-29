import {Component, OnInit} from '@angular/core';
import {EstimateFormService} from './estimate-form.service';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {IEstimateDTO, IEstimateForm, IEstimatePdfDTO} from './estimate-form';
import {FormGroup} from '@angular/forms';
import {ToastService} from '@app/shared/services/toast.service';
import {DictionaryDTO, enumToDictionary} from '@app/shared/utils/dictionary';
import {JobType} from '@app/shared/enums/job-type';
import {Currency} from '@app/shared/enums/currency';
import {catchError, EMPTY, finalize} from 'rxjs';
import {IEstimateReportDto} from "./estimate-report-form/estimate-report-form";
import {EstimateReportFormComponent} from "./estimate-report-form/estimate-report-form.component";
import {AppDialogService} from "@shared/commons/dialog/dialog.service";

interface Dictionaries {
  jobTypes: DictionaryDTO<JobType, string>[];
  currencies: DictionaryDTO<Currency, string>[];
}

@Component({
  templateUrl: './estimate-form.component.html',
  styleUrls: ['./estimate-form.component.scss'],
  providers: [EstimateFormService],
})
export class EstimateFormComponent implements OnInit {
  form: FormGroup<IEstimateForm> = this.service.createForm();

  loadingSpinner = false;

  constructor(
    private service: EstimateFormService,
    private conf: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private toast: ToastService,
    private dialog: AppDialogService
  ) {}

  ngOnInit(): void {
    let data: IEstimatePdfDTO | null = this.conf.data?.data;
    if (data === null || data === undefined) {
      return;
    }
    this.service.patchValues(this.form, this.service.formatDataFromPdf(data));
  }

  addJob(): void {
    this.form.controls.jobs.controls.unshift(this.service.getJobGroup());
  }

  removeJob(index: number): void {
    this.form.controls.jobs.controls.splice(index, 1);
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
      .save(this.form.getRawValue() as IEstimateDTO)
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

  dictionaries: Dictionaries = {
    jobTypes: enumToDictionary(JobType, 'enum.JobType'),
    currencies: enumToDictionary(Currency, 'enum.Currency'),
  };

  getReportingId(): string {
    const reportingId: string = this.form.getRawValue().reportingId;
    if (reportingId === null || reportingId === undefined) return null;
    else return reportingId;
  }

  openModalForm(reportId: string, data?: IEstimateReportDto): void {
    const newData: IEstimateReportDto = {
      pdfName: reportId,
      content: data?.content ?? ''
    };
    this.dialog
        .open(EstimateReportFormComponent, {
            data: {
              data: newData,
            },
            showHeader: false,
            closable: false,
            width: undefined,
            modal: true,
        }
      );
  }

}
