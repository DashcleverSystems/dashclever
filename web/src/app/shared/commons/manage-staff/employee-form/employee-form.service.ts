import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IEmployee, Workplace } from '@app/shared/models/employee';
import { IEmployeeForm } from './employee-form.component';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";

@Injectable()
export class EmployeeFormService {
  constructor(private fb: FormBuilder, private http: HttpClient) {}

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

  createEmployee(employee: IEmployee): Observable<IEmployee> {
    return this.http.post<IEmployee>(`/api/employee`, employee);
  }

  updateEmployee(employee: IEmployee): Observable<IEmployee> {
    return this.http.put<IEmployee>(`/api/employee/${employee.id}`, employee);
  }
}
