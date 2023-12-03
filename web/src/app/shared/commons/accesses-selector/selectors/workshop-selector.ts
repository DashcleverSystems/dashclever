import { Component, OnDestroy, OnInit } from '@angular/core';
import { SelectorListComponent } from './template/selector.template';
import { IWorkshop } from '@app/shared/models/workshop';
import { Store } from '@ngrx/store';
import {
  debounceTime,
  distinctUntilChanged,
  forkJoin,
  map,
  Observable,
  of,
  Subscription,
  take,
  takeUntil,
  tap,
} from 'rxjs';
import {
  getSelectedWorkshop,
  getWorkshops,
} from '@app/core/store/core-store.selectors';
import { coreStoreActions } from '@app/core/store/core-store.actions';
import { AccountRestApiService } from '@api/services/accountRestApi.service';
import { AccessDto } from '@api/models/accessDto';
import { WorkshopCreatedNotifier } from '@shared/commons/workshop/workshop-creator/workshop-creator.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-workshop-selector',
  templateUrl: './template/selector.template.html',
  styleUrls: ['./template/selector.template.scss'],
})
export class WorkshopSelectorComponent
  extends SelectorListComponent<Omit<IWorkshop, 'accesses'>>
  implements OnInit, OnDestroy
{
  override itemList: Observable<Omit<IWorkshop, 'accesses'>[]> = of([]);
  override value: string = 'workshopName';
  override title: string = 'components.accessesSelector.workshops.title';
  override itemName: string = 'Workshop';

  private workshopCreatedSub = this.listenToWorkshopCreation();

  constructor(
    private store: Store,
    private accountRestApi: AccountRestApiService,
    private workshopCreatedNotifier: WorkshopCreatedNotifier,
    private http: HttpClient,
  ) {
    super();
  }

  ngOnInit(): void {
    this.itemList = this.store.select(getWorkshops).pipe(
      takeUntil(this.destroy$),
      debounceTime(100),
      tap((list) => {
        this.visible = list.length > 0;
      }),
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

  private listenToWorkshopCreation(): Subscription {
    return this.workshopCreatedNotifier.workshopCreatedListener.subscribe(
      () => {
        this.http
          .get<IWorkshop[]>('api/account/access')
          .subscribe((accesses: IWorkshop[]) => {
            this.store.dispatch(
              coreStoreActions.changeWorkshops({ workshops: accesses }),
            );
          });
      },
    );
  }

  override defineInitialValue(): void {
    const currentAccessWorkshopId$ = this.accountRestApi
      .currentUser()
      .pipe(map((access: AccessDto) => access.workshopId));
    const displayedWorkshops$ = this.itemList.pipe(take(1));
    forkJoin([displayedWorkshops$, currentAccessWorkshopId$]).subscribe(
      ([displayedWorkshops, currentAccessWorkshopId]) => {
        const exists = displayedWorkshops.find(
          (acc) => acc.workshopId === currentAccessWorkshopId,
        );
        if (exists) {
          this.selectSpecificItem(exists);
          this.store.dispatch(
            coreStoreActions.selectWorkshopByWorkshopId({
              workshopId: exists.workshopId,
            }),
          );
        }
      },
    );
  }

  override onClick(index: number, workshop: IWorkshop | undefined): void {
    this.store.dispatch(
      coreStoreActions.selectWorkshop({ workshop: workshop ?? undefined }),
    );
  }

  override ngOnDestroy() {
    super.ngOnDestroy();
    this.workshopCreatedSub.unsubscribe();
  }
}
