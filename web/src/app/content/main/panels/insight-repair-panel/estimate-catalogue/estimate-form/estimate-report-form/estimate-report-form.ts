import {FormControl} from "@angular/forms";
import {UUID} from "angular2-uuid";

export interface IEstimateReportDto {
  reportingId: UUID;
  description: string;
}

export interface IEstimateReportForm {
  reportingId: FormControl<UUID | null>;
  description: FormControl<string | null>;
}
