import { Component, OnInit } from '@angular/core';
import { SelectorListComponent } from './template/selector.template';
import { IAccess } from '@shared/models/accesses';
import {
  debounceTime,
  distinctUntilChanged,
  map,
  Observable,
  of,
  skipWhile,
  take,
  takeUntil,
  tap,
} from 'rxjs';
import { AccessesSelectorComponentStore } from '@shared/commons/accesses-selector/access-selector.store';
import { IWorkshop } from '@shared/models/workshop';

@Component({
  selector: 'app-user-selector',
  templateUrl: './template/selector.template.html',
  styleUrls: ['./template/selector.template.scss'],
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

  constructor(private accessesStore: AccessesSelectorComponentStore) {
    super();
  }

  ngOnInit(): void {
    this.itemList = this.accessesStore.selectedWorkshopAccesses$.pipe(
      takeUntil(this.accessesStore.destroy$),
      distinctUntilChanged(),
      debounceTime(100),
      tap((accesses: IAccess[]) => (this.visible = accesses.length > 0)),
    );

    this.accessesStore.selectedAccess$
      .pipe(takeUntil(this.accessesStore.destroy$), distinctUntilChanged())
      .subscribe((access: IAccess | undefined) => this.selectAccess(access));
  }

  private selectAccess(access: IAccess | undefined) {
    if (!access) {
      this.removeSelected();
      return;
    }
    this.selectSpecificItem(access);
  }

  override onClick(index: number, access: IAccess | undefined): void {
    if (!access) {
      this.removeSelected();
      this.accessesStore.changeAccess(undefined);
      return;
    }
    this.accessesStore.selectedWorkshop$
      .pipe(take(1))
      .subscribe((workshop: IWorkshop) =>
        this.accessesStore.changeAccess({
          ...access,
          workshopId: workshop.workshopId,
        }),
      );
  }

  override defineInitialValue(): void {}
}
