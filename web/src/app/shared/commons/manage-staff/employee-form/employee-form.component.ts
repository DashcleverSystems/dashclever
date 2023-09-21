import { Component, OnInit } from '@angular/core';

import { FormControl, FormGroup } from '@angular/forms';
import { IEmployee, Workplace } from '@shared/models/employee';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { DictionaryDTO, enumToDictionary } from '@shared/utils/dictionary';
import { select, Store } from '@ngrx/store';
import { getSelectedWorkshop } from '@core/store/core-store.selectors';
import { EMPTY, Subject, switchMap, take } from 'rxjs';
import { ManageStaffStore } from '@shared/commons/manage-staff/manage-staff.store';
import { EmployeeFormService } from './employee-form.service';

@Component({
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.scss'],
  providers: [EmployeeFormService],
})
export class EmployeeFormComponent implements OnInit {
  constructor(
    public ref: DynamicDialogRef,
    private conf: DynamicDialogConfig,
    private store: Store,
    private manageStaffStore: ManageStaffStore,
    private service: EmployeeFormService
  ) {}

  form: FormGroup<IEmployeeForm> = this.service.createEmployeeForm(
    this.conf.data.employee
  );

  isCreatingNewEmployee =
    this.conf.data.employee === undefined || this.conf.data.employee === null;

  loadingSpinner = false;

  dictionaries: Dictionaries = {
    workplaces: enumToDictionary(Workplace, 'enum.Workplace'),
  };

  private destroy$ = new Subject<void>();

  ngOnInit(): void {}

  submit() {
    if (this.form.invalid) {
      return;
    }

    this.store
      .pipe(
        select(getSelectedWorkshop),
        take(1),
        switchMap((workshop) => {
          console.log(workshop);
          if (!workshop) {
            return EMPTY;
          }

          const employee: IEmployee = {
            ...this.form.getRawValue(),
            id: undefined,
            workshopId: workshop.workshopId,
            firstName: this.form.controls.firstName.value ?? '',
            workplace: this.form.controls.workplace.value ?? Workplace.LABOUR,
          };

          return this.service.createEmployee(employee);
        })
      )
      .subscribe((employee) => {
        this.ref.close(employee);
      });
  }

  onDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

export interface IEmployeeForm {
  firstName: FormControl<string | null>;
  lastName: FormControl<string | null>;
  workplace: FormControl<Workplace | null>;
}

interface Dictionaries {
  workplaces: DictionaryDTO<Workplace, string>[];
}
