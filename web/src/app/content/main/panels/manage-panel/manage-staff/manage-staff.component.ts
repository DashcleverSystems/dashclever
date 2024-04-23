import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil, distinctUntilChanged } from 'rxjs';
import { EmployeeFormComponent } from '@app/content/main/panels/manage-panel/manage-staff/employee-form/employee-form.component';
import { isEqual } from 'lodash';
import { ManageStaffStore } from './manage-staff.store';
import { EmployeeDto } from 'generated/openapi';
import { AppDialogService } from '@app/shared/commons/dialog/dialog.service';
import CoreStore from '@app/core/store/core-store';

@Component({
  selector: 'app-manage-staff',
  templateUrl: './manage-staff.component.html',
  styleUrls: ['./manage-staff.component.scss'],
})
export class ManageStaffComponent implements OnInit, OnDestroy {
  employees: EmployeeDto[] = [];

  get isMobile() {
    return this.coreStore.mobile();
  }

  private destroy$ = new Subject<void>();
  private coreStore = inject(CoreStore);

  constructor(
    private manageStore: ManageStaffStore,
    private dialogService: AppDialogService,
  ) {}

  ngOnInit(): void {
    this.subscribeEmployees();
    this.manageStore.loadCollection();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  editEmployee(employee: EmployeeDto): void {
    this.openEmployeeForm(employee);
  }

  addEmployee(): void {
    this.openEmployeeForm(null);
  }

  private subscribeEmployees(): void {
    this.manageStore.employees$
      .pipe(takeUntil(this.destroy$), distinctUntilChanged(isEqual))
      .subscribe((employees) => {
        this.employees = [...employees];
      });
  }

  private openEmployeeForm(employee: EmployeeDto | null): void {
    const ref = this.dialogService.open(EmployeeFormComponent, {
      data: {
        employee: employee,
      },
      closable: false,
      width: this.isMobile ? '100svw' : undefined,
      style: { 'min-width': !this.isMobile ? '40svw' : undefined },
    });

    ref.onClose.subscribe((res) => res && this.manageStore.loadCollection());
  }
}
