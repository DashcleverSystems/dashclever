import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, switchMap, take} from "rxjs";
import {IEstimatePdfDTO} from "@shared/commons/estimate-catalogue/estimate-form/estimate-form";
import {DialogService} from "primeng/dynamicdialog";
import {IEmployee} from "@shared/models/employee";
import {Store} from "@ngrx/store";
import {getSelectedWorkshop} from "@core/store/core-store.selectors";
import {ManageStaffStore} from "@shared/commons/manage-staff/manage-staff.store";

@Injectable()
export class EstimateCatalogueService {
  constructor(
    private http: HttpClient,
    private dialogService: DialogService,
    private store: Store,
    private manageStaffStore: ManageStaffStore
  ) {}

  getEmployees(): Observable<IEmployee> {
    return this.store.select(getSelectedWorkshop).pipe(
      take(1),
      switchMap(workshop => this.http.get<IEmployee>(`/api/workshop/${workshop?.workshopId}/employee`))
    )
  }
}
