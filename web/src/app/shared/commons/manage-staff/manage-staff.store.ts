import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { IEmployee } from '@shared/models/employee';
import {switchMap, tap} from 'rxjs';
import { ManageStaffService } from './manage-staff.service';

interface ManageStaffStoreState {
  employees: IEmployee[];
}

@Injectable()
export class ManageStaffStore extends ComponentStore<ManageStaffStoreState> {
  constructor(private service: ManageStaffService) {
    super({ employees: [] });
  }

  readonly loadCollection = this.effect((effect$) =>
    effect$.pipe(
        switchMap(() => this.service.getEmployees()
      ),
      tap((employees) => this.loadEmployees(employees))
    )
  );

  readonly loadEmployees = this.updater((state, employees: IEmployee[]) => ({
    ...state,
    employees: employees,
  }));

  readonly employees$ = this.select((state) => state.employees);
}
