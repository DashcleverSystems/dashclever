import { Component, OnInit } from '@angular/core';
import { SelectorListComponent } from './template/selector.template';
import { IAccess } from '@app/shared/models/accesses';
import {
  Observable,
  combineLatest,
  debounceTime,
  distinctUntilChanged,
  map,
  of,
  skipWhile,
  switchMap,
  take,
  takeUntil,
} from 'rxjs';
import { Store } from '@ngrx/store';
import {
  getSelectedAccess,
  getSelectedWorkshop,
  getUsers,
} from '@app/core/store/core-store.selectors';
import { coreStoreActions } from '@app/core/store/core-store.actions';
import { isEqual } from 'lodash';
import { UserSelectorService } from './user-selector.service';

@Component({
  selector: 'app-user-selector',
  templateUrl: './template/selector.template.html',
  styleUrls: ['./template/selector.template.scss'],
  providers: [UserSelectorService],
})
export class UserSelectorComponent
  extends SelectorListComponent<IAccess>
  implements OnInit
{
  override itemList: Observable<IAccess[]> = of([]);
  override value: string = 'employeeFirstName';
  override alternativeValue: string = 'accessesSelector.owner';
  override title: string = 'components.accessesSelector.users.title';
  override itemName: string = 'User';

  constructor(private store: Store, private service: UserSelectorService) {
    super();
  }

  ngOnInit(): void {
    this.itemList = this.store.select(getUsers).pipe(
      takeUntil(this.destroy$),
      skipWhile((users) => !users),
      debounceTime(100),
      map((users) => {
        if (Array.isArray(users) && users.length > 0) {
          this.visible = true;
          return users;
        } else {
          this.visible = false;
          return [];
        }
      })
    );

    this.store
      .select(getSelectedAccess)
      .pipe(distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((item) => {
        if (!item) {
          this.removeSelected();
        }
      });
  }

  override onClick(index: number, access: IAccess | undefined): void {
    this.store
      .select(getSelectedWorkshop)
      .pipe(
        take(1),
        switchMap((selectedWorkshop) =>
          this.service.selectUser({
            workshopId: selectedWorkshop?.workshopId ?? null,
            employeeId: access?.employeeId ?? null,
          })
        )
      )
      .subscribe(() =>
        this.store.dispatch(coreStoreActions.selectAccess({ access }))
      );
  }

  override defineInitialValue(): void {
    combineLatest([this.itemList, this.store.select(getSelectedAccess)])
      .pipe(take(1))
      .subscribe(([items, access]) => {
        const exists = items.find((acc) => isEqual(acc, access));
        if (exists) {
          this.selectSpecificItem(exists);
        }
      });
  }
}
