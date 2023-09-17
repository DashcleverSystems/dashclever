import { Injectable } from '@angular/core';
import { IAccess } from '@app/shared/models/accesses';
import { ComponentStore } from '@ngrx/component-store';

interface ManageStaffStoreState {
  workers: IAccess[];
}

@Injectable()
export class ManageStaffStore extends ComponentStore<ManageStaffStoreState> {
  constructor() {
    super({ workers: [] });
  }

  loadWorkers = this.updater((state, workers: IAccess[]) => ({
    ...state,
    workers: workers,
  }));

  workers$ = this.select((state) => (state.workers ? state.workers : []));
}
