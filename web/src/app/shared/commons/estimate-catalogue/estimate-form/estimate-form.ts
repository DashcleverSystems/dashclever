import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { Currency } from '@app/shared/enums/currency';
import { JobType } from '@app/shared/enums/job-type';

export interface IEstimatedDTO {
  estimateId: string;
  vehicleInfo: IEstimatedVehicleInfo;
  paintInfo: IEstimatedPaintInfo;
  jobs: IEstimatedJob[];
}

export interface IEstimatedPdfDTO {
  uniqueId: string;
  vehicleInfo: IEstimatedVehicleInfo;
  paint: {
    baseColorWithCode: string;
    varnishingPaintInfo: string[];
  };
  labourJobs: Omit<IEstimatedJob, 'jobType'>[];
  varnishingJobs: Omit<IEstimatedJob, 'jobType'>[];
}

export interface IEstimatedForm {
  estimateId: FormControl<string | null>;
  vehicleInfo: FormGroup<IEstimatedVehicleInfoForm>;
  paintInfo: FormGroup<IEstimatedPaintInfoForm>;
  jobs: FormArray<FormGroup<IEstimatedJobForm>>;
}

export interface IEstimatedVehicleInfo {
  registration: string;
  brand: string;
  model: string;
}

export interface IEstimatedVehicleInfoForm {
  registration: FormControl<string | null>;
  brand: FormControl<string | null>;
  model: FormControl<string | null>;
}

export interface IEstimatedPaintInfo {
  baseColorWithCode: string;
  varnishingPaintInfo: string;
}

export interface IEstimatedPaintInfoForm {
  baseColorWithCode: FormControl<string | null>;
  varnishingPaintInfo: FormControl<string | null>;
}

export interface IEstimatedJob {
  name: string;
  manMinutes: number;
  worth: IEstimatedWorth;
  jobType: JobType;
}

export interface IEstimatedJobForm {
  name: FormControl<string | null>;
  manMinutes: FormControl<number | null>;
  worth: FormGroup<IEstimatedWorthForm>;
  jobType: FormControl<string | JobType | null>;
}

export interface IEstimatedWorth {
  denomination: number;
  currency: Currency;
}

export interface IEstimatedWorthForm {
  denomination: FormControl<number | null>;
  currency: FormControl<string | Currency | null>;
}
