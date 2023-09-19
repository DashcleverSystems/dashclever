import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import {IEmployee} from "@shared/models/employee";

interface ManageStaffStoreState {
  employees: IEmployee[];
}

@Injectable()
export class ManageStaffStore extends ComponentStore<ManageStaffStoreState> {
  constructor() {
    super({ employees: [] });
  }

  loadEmployees = this.updater((state, workers: IEmployee[]) => ({
    ...state,
    employees: workers,
  }));

  employees$ = this.select((state) => (state.employees ? state.employees : []));
}
