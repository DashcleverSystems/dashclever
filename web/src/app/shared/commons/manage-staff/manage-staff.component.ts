import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { isMobile } from '@app/core/store/core-store.selectors';
import { Subject, takeUntil, distinctUntilChanged } from 'rxjs';
import { IEmployee } from '@shared/models/employee';
import { DialogService } from 'primeng/dynamicdialog';
import { EmployeeFormComponent } from '@shared/commons/manage-staff/employee-form/employee-form.component';
import { isEqual } from 'lodash';
import { ManageStaffStore } from './manage-staff.store';

@Component({
  selector: 'app-manage-staff',
  templateUrl: './manage-staff.component.html',
  styleUrls: ['./manage-staff.component.scss'],
})
export class ManageStaffComponent implements OnInit, OnDestroy {
  employees: IEmployee[] = [];

  private isMobile = false;

  private destroy$ = new Subject<void>();

  constructor(
    private store: Store,
    private manageStore: ManageStaffStore,
    private dialogService: DialogService
  ) {}

  ngOnInit(): void {
    this.store
      .select(isMobile)
      .pipe(takeUntil(this.destroy$), distinctUntilChanged())
      .subscribe((mobile) => (this.isMobile = mobile));

    this.subscribeEmployees();
    this.manageStore.loadCollection();
  }

  editEmployee(employee: IEmployee): void {
    this.openEmployeeForm(employee);
  }

  addEmployee(): void {
    this.openEmployeeForm(null);
  }

  private subscribeEmployees(): void {
    this.manageStore.employees$
      .pipe(takeUntil(this.destroy$), distinctUntilChanged(isEqual))
      .subscribe((employees) => {
        this.employees = employees;
      });
  }

  private openEmployeeForm(employee: IEmployee | null): void {
    const ref = this.dialogService.open(EmployeeFormComponent, {
      data: {
        employee: employee,
      },
      showHeader: false,
      closable: false,
      width: this.isMobile ? '100svw' : undefined,
      style: { 'min-width': !this.isMobile ? '40svw' : undefined },
      modal: true,
    });

    ref.onClose.subscribe((res) => res && this.manageStore.loadCollection());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
