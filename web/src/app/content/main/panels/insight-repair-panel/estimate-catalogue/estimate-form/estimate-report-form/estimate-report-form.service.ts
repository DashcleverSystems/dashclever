import {Injectable} from "@angular/core";
import {IEstimateReportDto, IEstimateReportForm} from "./estimate-report-form";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable()
export class EstimateReportFormService {

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
  ) {}

  createForm(
    data?: Partial<IEstimateReportDto>
  ): FormGroup<IEstimateReportForm> {
    return this.fb.group({
      pdfName: this.fb.control<string | null>(
        data?.pdfName ?? null
      ),
      content: this.fb.control<string|null>(
        data?.content ?? null,
        [Validators.required, Validators.maxLength(1000)]
      )
    });
  }

  patchValues(form: FormGroup<IEstimateReportForm>, data: IEstimateReportDto) {
    form.controls.pdfName.patchValue(data.pdfName);
    form.controls.content.patchValue(data.content);
  }

  save(data: IEstimateReportDto): Observable<any> {
    return this.http.post("/api/estimate/report", data);
  }
}
