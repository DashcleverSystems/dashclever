import { Component, OnInit } from '@angular/core';
import { SelectorListComponent } from './template/selector.template';
import { IAccess } from '@app/shared/models/accesses';
import { Observable, map, of, skipWhile, takeUntil } from 'rxjs';
import { Store } from '@ngrx/store';
import { getUsers } from '@app/core/store/core-store.selectors';

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
  override title: string = 'Choose User';
  override itemName: string = 'User';

  constructor(private store: Store) {
    super();
  }

  ngOnInit(): void {
    this.itemList = this.store.select(getUsers).pipe(
      takeUntil(this.destroy$),
      skipWhile((users) => !users),
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
  }

  override onClick(): void {}
}
