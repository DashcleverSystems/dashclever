import { Component, OnDestroy, OnInit } from '@angular/core';
import { ManageStaffStore } from './manage-staff.store';
import { Store, select } from '@ngrx/store';
import {getSelectedWorkshop, isMobile} from '@app/core/store/core-store.selectors';
import {Observable, Subject, map, takeUntil, distinctUntilChanged} from 'rxjs';
import { IAccess } from '@app/shared/models/accesses';
import {HttpClient} from "@angular/common/http";
import {IEmployee} from "@shared/models/employee";
import {DialogService} from "primeng/dynamicdialog";
import {EmployeeFormComponent} from "@shared/commons/manage-staff/employee-form/employee-form.component";

@Component({
  selector: 'app-manage-staff',
  templateUrl: './manage-staff.component.html',
  styleUrls: ['./manage-staff.component.scss'],
  providers: [ManageStaffStore],
})
export class ManageStaffComponent implements OnInit, OnDestroy {
  employees: Observable<IEmployee[]> = this.manageStore.employees$;

  private isMobile = false
  private destroy$ = new Subject<void>();

  constructor(
    private store: Store,
    private manageStore: ManageStaffStore,
    private dialogService: DialogService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.store
      .pipe(
        takeUntil(this.destroy$),
        select(getSelectedWorkshop),
        map((workshop) => this.http.get<IEmployee[]>(`/api/workshop/${workshop!!.workshopId}/employee`))
      )
      .subscribe((employees) => employees && this.manageStore.loadEmployees(employees));
    this.store
        .select(isMobile)
        .pipe(takeUntil(this.destroy$), distinctUntilChanged())
        .subscribe((mobile) => (this.isMobile = mobile));
  }

  editEmployee(employee: IEmployee): void {
    this.openEmployeeForm(employee);
  }

  addEmployee(): void {
    this.openEmployeeForm(null);
  }

  private openEmployeeForm(employee: IEmployee | null): void {
    this.dialogService.open(EmployeeFormComponent, {
      data : {
        employee: employee
      },
      showHeader: false,
      closable: false,
      width: this.isMobile ? '100svw' : undefined,
      style: {"min-width": !this.isMobile ? "40svw" : undefined },
      modal: true,
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
