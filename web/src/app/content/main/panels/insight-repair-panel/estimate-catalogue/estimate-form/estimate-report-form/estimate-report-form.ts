import {FormControl} from "@angular/forms";

export interface IEstimateReportDto {
  pdfName: string;
  content: string;
}

export interface IEstimateReportForm {
  pdfName: FormControl<string | null>;
  content: FormControl<string | null>;
}
