import {Component, OnDestroy} from '@angular/core';

import { FormControl, FormGroup } from '@angular/forms';
import { IEmployee, Workplace } from '@shared/models/employee';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { DictionaryDTO, enumToDictionary } from '@shared/utils/dictionary';
import {Store} from '@ngrx/store';
import {Observable, Subject} from 'rxjs';
import { EmployeeFormService } from './employee-form.service';

@Component({
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.scss'],
  providers: [EmployeeFormService],
})
export class EmployeeFormComponent implements OnDestroy {
  constructor(
    public ref: DynamicDialogRef,
    private conf: DynamicDialogConfig,
    private store: Store,
    private service: EmployeeFormService
  ) {}

  form: FormGroup<IEmployeeForm> = this.service.createEmployeeForm(
    this.conf.data.employee
  );

  private employee: IEmployee | undefined | null = this.conf.data.employee

  isCreatingNewEmployee =
    this.employee === undefined || this.employee === null;

  loadingSpinner = false;

  dictionaries: Dictionaries = {
    workplaces: enumToDictionary(Workplace, 'enum.Workplace'),
  };

  private destroy$ = new Subject<void>();

  submit() {
    if (this.form.invalid) {
      return;
    }

      this.createOrUpdateEmployee().subscribe((employee) => {
          this.ref.close(employee);
      });
  }

    private createOrUpdateEmployee(): Observable<IEmployee> {
        const employee: IEmployee = {
            ...this.form.getRawValue(),
            id: this.employee?.id,
            firstName: this.form.controls.firstName.value ?? '',
            workplace: this.form.controls.workplace.value ?? Workplace.LABOUR,
        };
        if (this.isCreatingNewEmployee) {
            return this.service.createEmployee(employee);
        } else {
            return this.service.updateEmployee(employee);
        }
    }

  ngOnDestroy(): void {
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
