import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  IEstimatedDTO,
  IEstimatedForm,
  IEstimatedJob,
  IEstimatedJobForm,
  IEstimatedPaintInfo,
  IEstimatedPaintInfoForm,
  IEstimatedPdfDTO,
  IEstimatedVehicleInfo,
  IEstimatedVehicleInfoForm,
  IEstimatedWorth,
  IEstimatedWorthForm,
} from './estimate-form';
import { Currency } from '@app/shared/enums/currency';
import { JobType } from '@app/shared/enums/job-type';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class EstimateFormService {
  constructor(private fb: FormBuilder, private http: HttpClient) {}

  createForm(
    data?: Partial<IEstimatedDTO> | undefined
  ): FormGroup<IEstimatedForm> {
    return this.fb.group({
      estimateId: this.fb.control<string | null>(
        data?.estimateId ?? null,
        Validators.required
      ),
      vehicleInfo: this.getVehicleInfoGroup(data?.vehicleInfo),
      paintInfo: this.getVehiclePaintInfoGroup(data?.paintInfo),
      jobs: this.fb.array<FormGroup<IEstimatedJobForm>>(
        data?.jobs && Array.isArray(data.jobs) && data.jobs.length > 0
          ? Array.from(data.jobs, (job) => this.getJobGroup(job))
          : [this.getJobGroup()]
      ),
    });
  }

  save(data: IEstimatedDTO): Observable<any> {
    return this.http.post('/api/estimatecatalogue', data);
  }

  formatDataFromPdf(data: IEstimatedPdfDTO): IEstimatedDTO {
    return {
      estimateId: data.uniqueId,
      vehicleInfo: data.vehicleInfo,
      paintInfo: {
        ...data.paint,
        varnishingPaintInfo: data.paint.varnishingPaintInfo.join(', '),
      },
      jobs: [
        ...Array.from(data.labourJobs, (job) => ({
          ...job,
          worth: {
            ...job.worth,
            denomination: job.worth.denomination / 100,
          },
          jobType: JobType.LABOUR,
        })),
        ...Array.from(data.varnishingJobs, (job) => ({
          ...job,
          worth: {
            ...job.worth,
            denomination: job.worth.denomination / 100,
          },
          jobType: JobType.VARNISHING,
        })),
      ],
    };
  }

  patchValues(form: FormGroup<IEstimatedForm>, data: IEstimatedDTO) {
    form.controls.estimateId.patchValue(data.estimateId);
    form.controls.paintInfo.patchValue(data.paintInfo);
    form.controls.vehicleInfo.patchValue(data.vehicleInfo);
    form.controls.jobs.clear();
    data.jobs.forEach((job) =>
      form.controls.jobs.controls.push(this.getJobGroup(job))
    );
  }

  mapValuesBeforeSave(data: IEstimatedDTO) {
    return {
      ...data,
      jobs: data.jobs.map((job) => ({
        ...job,
        worth: {
          ...job.worth,
          denomination: job.worth.denomination * 100,
        },
      })),
    };
  }

  getVehicleInfoGroup(
    data?: Partial<IEstimatedVehicleInfo>
  ): FormGroup<IEstimatedVehicleInfoForm> {
    return this.fb.group({
      registration: this.fb.control<string | null>(
        data?.registration ?? null,
        Validators.required
      ),
      brand: this.fb.control<string | null>(
        data?.brand ?? null,
        Validators.required
      ),
      model: this.fb.control<string | null>(
        data?.model ?? null,
        Validators.required
      ),
    });
  }

  getVehiclePaintInfoGroup(
    data?: Partial<IEstimatedPaintInfo>
  ): FormGroup<IEstimatedPaintInfoForm> {
    return this.fb.group({
      baseColorWithCode: this.fb.control<string | null>(
        data?.baseColorWithCode ?? null,
        Validators.required
      ),
      varnishingPaintInfo: this.fb.control<string | null>(
        data?.varnishingPaintInfo ?? null,
        Validators.required
      ),
    });
  }

  getWorthJobGroup(
    data?: Partial<IEstimatedWorth>
  ): FormGroup<IEstimatedWorthForm> {
    return this.fb.group({
      denomination: this.fb.control<number | null>(
        data?.denomination ? data?.denomination / 100 : null,
        Validators.required
      ),
      currency: this.fb.control<string | Currency | null>(
        data?.currency ?? null,
        Validators.required
      ),
    });
  }

  getJobGroup(data?: Partial<IEstimatedJob>): FormGroup<IEstimatedJobForm> {
    return this.fb.group({
      name: this.fb.control<string | null>(
        data?.name ?? null,
        Validators.required
      ),
      manMinutes: this.fb.control<number | null>(
        data?.manMinutes ?? null,
        Validators.required
      ),
      worth: this.getWorthJobGroup(data?.worth),
      jobType: this.fb.control<string | JobType | null>(
        data?.jobType ?? null,
        Validators.required
      ),
    });
  }
}
