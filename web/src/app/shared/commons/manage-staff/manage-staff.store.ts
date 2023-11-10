import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import {switchMap, tap} from 'rxjs';
import {EmployeeDto, EmployeeApiService} from 'generated/openapi';

interface ManageStaffStoreState {
  employees: EmployeeDto[];
}

@Injectable()
export class ManageStaffStore extends ComponentStore<ManageStaffStoreState> {
  constructor(private restApiService: EmployeeApiService) {
    super({ employees: [] });
  }

  readonly loadCollection = this.effect((effect$) =>
    effect$.pipe(
        switchMap(() => this.restApiService.getAll()),
        tap((employeeSet) => {
          this.loadEmployees(Array.from(employeeSet))
        })
    )
  );

  readonly loadEmployees = this.updater((state, employees: EmployeeDto[]) => ({
    ...state,
    employees: employees,
  }));

  readonly employees$ = this.select((state) => state.employees);
}
