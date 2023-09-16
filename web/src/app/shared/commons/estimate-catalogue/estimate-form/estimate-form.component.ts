import { Component, OnInit } from '@angular/core';
import { EstimateFormService } from './estimate-form.service';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { IEstimatedDTO, IEstimatedForm } from './estimate-form';
import { FormGroup } from '@angular/forms';
import { ToastService } from '@app/shared/services/toast.service';
import { DictionaryDTO, enumToDictionary } from '@app/shared/utils/dictionary';
import { JobType } from '@app/shared/enums/job-type';
import { Currency } from '@app/shared/enums/currency';

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
  type: 'CREATE' | 'GENERATE' = this.conf.data.type;

  form: FormGroup<IEstimatedForm> = this.service.createForm(
    this.conf.data.data as Partial<IEstimatedDTO>
  );

  get deleteJobButtonEnable(): boolean {
    return this.form.controls.jobs.controls.length > 1;
  }

  constructor(
    private service: EstimateFormService,
    private conf: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private toast: ToastService
  ) {}

  ngOnInit(): void {}

  addJob(): void {
    this.form.controls.jobs.controls.push(this.service.getJobGroup());
  }

  removeJob(index: number): void {
    this.form.controls.jobs.controls.splice(index, 1);
  }

  submitForm(): void {
    if (this.form.invalid) {
      this.toast.error({
        title: 'error.errorOccurred',
        message: 'error.form.validationError',
        translate: true,
      });
      return;
    }

    console.log(this.form.getRawValue());
  }

  dictionaries: Dictionaries = {
    jobTypes: enumToDictionary(JobType, 'enum.JobType'),
    currencies: enumToDictionary(Currency, 'enum.Currency'),
  };
}
