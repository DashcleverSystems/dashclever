import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { IEmployee } from '@shared/models/employee';
import { EMPTY, switchMap, tap } from 'rxjs';
import { ManageStaffService } from './manage-staff.service';
import { Store } from '@ngrx/store';
import { getSelectedWorkshop } from '@core/store/core-store.selectors';

interface ManageStaffStoreState {
  employees: IEmployee[];
}

@Injectable()
export class ManageStaffStore extends ComponentStore<ManageStaffStoreState> {
  constructor(private service: ManageStaffService, private store: Store) {
    super({ employees: [] });
  }

  readonly loadCollection = this.effect((effect$) =>
    effect$.pipe(
      switchMap(() => this.store.select(getSelectedWorkshop)),
      switchMap((selectedWorkshop) =>
        selectedWorkshop
          ? this.service.getEmployees(selectedWorkshop.workshopId)
          : EMPTY
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
