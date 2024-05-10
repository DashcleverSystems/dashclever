import {Injectable} from "@angular/core";
import {IEstimateReportDto, IEstimateReportForm} from "./estimate-report-form";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UUID} from "angular2-uuid";
import {EstimateApiService} from "@api/services/estimateApi.service";
import {ReportDto} from "@api/models/reportDto";

@Injectable()
export class EstimateReportFormService {

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private service: EstimateApiService
  ) {}

  createForm(
    data?: Partial<IEstimateReportDto>
  ): FormGroup<IEstimateReportForm> {
    return this.fb.group({
      reportingId: this.fb.control<UUID | null>(
        data?.reportingId ?? null
      ),
      description: this.fb.control<string|null>(
        data?.description ?? null,
        [Validators.required, Validators.maxLength(1000)]
      )
    });
  }

  patchValues(form: FormGroup<IEstimateReportForm>, data: IEstimateReportDto) {
    form.controls.reportingId.patchValue(data.reportingId);
    form.controls.description.patchValue(data.description);
  }

  save(data: IEstimateReportDto): Observable<any> {
    return this.service.createReport(data as ReportDto);
  }
}
