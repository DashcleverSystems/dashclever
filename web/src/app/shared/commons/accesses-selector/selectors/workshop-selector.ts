import { Component, OnDestroy, OnInit } from '@angular/core';
import { SelectorListComponent } from './template/selector.template';
import { IWorkshop } from '@shared/models/workshop';
import {
  debounceTime,
  Observable,
  of,
  Subscription,
  takeUntil,
  tap,
} from 'rxjs';
import { WorkshopCreatedNotifier } from '@shared/commons/workshop/workshop-creator/workshop-creator.service';
import { AccessesSelectorComponentStore } from '@shared/commons/accesses-selector/access-selector.store';

@Component({
  selector: 'app-workshop-selector',
  templateUrl: './template/selector.template.html',
  styleUrls: ['./template/selector.template.scss'],
})
export class WorkshopSelectorComponent
  extends SelectorListComponent<Omit<IWorkshop, 'accesses'>>
  implements OnInit, OnDestroy
{
  override itemList: Observable<IWorkshop[]> = of([]);
  override value: string = 'workshopName';
  override title: string = 'components.accessesSelector.workshops.title';
  override itemName: string = 'Workshop';

  private workshopCreatedSub = this.listenToWorkshopCreation();

  constructor(
    private accessesStore: AccessesSelectorComponentStore,
    private workshopCreatedNotifier: WorkshopCreatedNotifier,
  ) {
    super();
  }

  ngOnInit(): void {
    this.itemList = this.accessesStore.workshops$.pipe(
      takeUntil(this.accessesStore.destroy$),
      debounceTime(100),
      tap((list) => {
        this.visible = list.length > 0;
      }),
    );
    this.accessesStore.selectedWorkshop$
      .pipe(takeUntil(this.accessesStore.destroy$))
      .subscribe((workshop: IWorkshop | undefined) =>
        this.selectWorkshop(workshop),
      );
    this.accessesStore.loadAccesses();
  }

  private listenToWorkshopCreation(): Subscription {
    return this.workshopCreatedNotifier.workshopCreatedListener.subscribe(() =>
      this.accessesStore.loadAccesses(),
    );
  }

  override defineInitialValue(): void {}

  private selectWorkshop(workshop: IWorkshop | undefined): void {
    this.removeSelected();
    if (workshop) {
      this.selectSpecificItem(workshop);
    }
  }

  override onClick(index: number, workshop: IWorkshop | undefined): void {
    this.accessesStore.selectWorkshop(workshop);
  }

  override ngOnDestroy() {
    super.ngOnDestroy();
    this.workshopCreatedSub.unsubscribe();
  }
}
