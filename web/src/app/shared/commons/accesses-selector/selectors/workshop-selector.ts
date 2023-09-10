import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { SelectorListComponent } from './template/selector.template';
import { IWorkshop } from '@app/shared/models/workshop';
import { Store } from '@ngrx/store';
import {
  Observable,
  combineLatest,
  debounceTime,
  distinctUntilChanged,
  of,
  take,
  takeUntil,
  tap,
} from 'rxjs';
import {
  getSelectedWorkshop,
  getWorkshops,
} from '@app/core/store/core-store.selectors';
import { coreStoreActions } from '@app/core/store/core-store.actions';
import { isEqual } from 'lodash';

@Component({
  selector: 'app-workshop-selector',
  templateUrl: './template/selector.template.html',
  styleUrls: ['./template/selector.template.scss'],
})
export class WorkshopSelectorComponent
  extends SelectorListComponent<Omit<IWorkshop, 'accesses'>>
  implements OnInit
{
  override itemList: Observable<Omit<IWorkshop, 'accesses'>[]> = of([]);
  override value: string = 'workshopName';
  override title: string = 'Choose WorkShop';
  override itemName: string = 'Workshop';

  constructor(private store: Store) {
    super();
  }

  ngOnInit(): void {
    this.itemList = this.store.select(getWorkshops).pipe(
      takeUntil(this.destroy$),
      debounceTime(100),
      tap((list) => {
        this.visible = list.length > 0;
      })
    );

    this.store
      .select(getSelectedWorkshop)
      .pipe(distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((item) => {
        if (!item) {
          this.removeSelected();
        }
      });
  }

  override defineInitialValue(): void {
    combineLatest([this.itemList, this.store.select(getSelectedWorkshop)])
      .pipe(take(1))
      .subscribe(([items, access]) => {
        const exists = items.find(
          (acc) => acc.workshopId === access?.workshopId
        );
        if (exists) {
          this.selectSpecificItem(exists);
        }
      });
  }

  override onClick(index: number, workshop: IWorkshop | undefined): void {
    this.store.dispatch(
      coreStoreActions.selectWorkshop({ workshop: workshop ?? undefined })
    );
  }
}
