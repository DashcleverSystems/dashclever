import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IEmployee, Workplace } from '@app/shared/models/employee';
import { IEmployeeForm } from './employee-form.component';
import {Observable} from "rxjs";
import {EmployeeApiService, EmployeeDto} from 'generated/openapi';

@Injectable()
export class EmployeeFormService {
  constructor(private fb: FormBuilder, private employeeApiService: EmployeeApiService) {
  }

  createEmployeeForm(employee: IEmployee): FormGroup<IEmployeeForm> {
    return this.fb.group({
      firstName: this.fb.control<string>(
        employee?.firstName ?? '',
        Validators.required
      ),
      lastName: this.fb.control<string | null>(employee?.lastName ?? null),
      workplace: this.fb.control<Workplace>(
        employee?.workplace ?? Workplace.LABOUR,
        Validators.required
      ),
    });
  }

  createEmployee(employee: EmployeeDto): Observable<EmployeeDto> {
    return this.employeeApiService.addEmployee(employee)
  }

  updateEmployee(employee: EmployeeDto): Observable<EmployeeDto> {
    return this.employeeApiService.changeEmployee(employee.id!!, employee)
  }
}
