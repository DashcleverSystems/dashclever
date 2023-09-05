import { Component, OnInit } from '@angular/core';
import { SelectorListComponent } from './template/selector.template';
import { IWorkshop } from '@app/shared/models/workshop';
import { Store } from '@ngrx/store';
import { Observable, of, takeUntil, tap } from 'rxjs';
import { getWorkshops } from '@app/core/store/core-store.selectors';

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
      tap(() => (this.visible = true))
    );
  }

  override onClick(): void {}
}
