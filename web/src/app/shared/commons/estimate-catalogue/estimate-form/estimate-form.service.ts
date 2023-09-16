import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  IEstimatedDTO,
  IEstimatedForm,
  IEstimatedJob,
  IEstimatedJobForm,
  IEstimatedPaintInfo,
  IEstimatedPaintInfoForm,
  IEstimatedVehicleInfo,
  IEstimatedVehicleInfoForm,
  IEstimatedWorth,
  IEstimatedWorthForm,
} from './estimate-form';
import { Currency } from '@app/shared/enums/currency';
import { JobType } from '@app/shared/enums/job-type';

@Injectable()
export class EstimateFormService {
  constructor(private fb: FormBuilder) {}

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
