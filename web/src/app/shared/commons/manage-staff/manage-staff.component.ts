import { Component, OnDestroy, OnInit } from '@angular/core';
import { ManageStaffStore } from './manage-staff.store';
import { Store, select } from '@ngrx/store';
import { getSelectedWorkshop } from '@app/core/store/core-store.selectors';
import { Observable, Subject, map, takeUntil } from 'rxjs';
import { IAccess } from '@app/shared/models/accesses';

@Component({
  selector: 'app-manage-staff',
  templateUrl: './manage-staff.component.html',
  styleUrls: ['./manage-staff.component.scss'],
  providers: [ManageStaffStore],
})
export class ManageStaffComponent implements OnInit, OnDestroy {
  workers: Observable<IAccess[]> = this.manageStore.workers$;

  private destroy$ = new Subject<void>();

  constructor(private store: Store, private manageStore: ManageStaffStore) {}

  ngOnInit(): void {
    this.store
      .pipe(
        takeUntil(this.destroy$),
        select(getSelectedWorkshop),
        map((workshop) => workshop?.accesses)
      )
      .subscribe((workers) => workers && this.manageStore.loadWorkers(workers));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
