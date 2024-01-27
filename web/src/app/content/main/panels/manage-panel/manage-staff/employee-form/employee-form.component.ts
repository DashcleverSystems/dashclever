import { Component, OnDestroy } from '@angular/core';

import { FormControl, FormGroup } from '@angular/forms';
import { Workplace } from '@shared/models/employee';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { DictionaryDTO, enumToDictionary } from '@shared/utils/dictionary';
import { Observable, Subject } from 'rxjs';
import { EmployeeFormService } from './employee-form.service';
import { EmployeeDto } from '../../../../../../../../generated/openapi';

@Component({
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.scss'],
  providers: [EmployeeFormService],
})
export class EmployeeFormComponent implements OnDestroy {
  constructor(
    public ref: DynamicDialogRef,
    private conf: DynamicDialogConfig,
    private service: EmployeeFormService,
  ) {}

  blockChars: RegExp = /^[^{}\\|"';:?!.,><#*-+~`=@$%^&()_-]+$/;

  form: FormGroup<IEmployeeForm> = this.service.createEmployeeForm(
    this.conf.data.employee,
  );

  private employee: EmployeeDto | undefined | null = this.conf.data.employee;

  isCreatingNewEmployee = this.employee === undefined || this.employee === null;

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

  private createOrUpdateEmployee(): Observable<EmployeeDto> {
    const employeeDto: EmployeeDto = {
      ...this.form.getRawValue(),
      id: this.employee?.id,
      firstName: this.form.controls.firstName.value ?? '',
      lastName: this.form.controls.lastName.value ?? '',
      workplace: this.form.controls.workplace.value ?? Workplace.LABOUR,
    };
    if (this.isCreatingNewEmployee) {
      return this.service.createEmployee(employeeDto);
    } else {
      return this.service.updateEmployee(employeeDto);
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
