import { ComponentStore } from '@ngrx/component-store';
import { Observable, Subscription, of, switchMap, tap } from 'rxjs';

export interface TableStoreState<T> {
  data: T[];
  totalElements: number;
}

export interface TableStoreMethods<T> {
  readonly data$: Observable<T[]>;
  readonly totalElements$: Observable<number>;

  setData(data: T[]): Subscription;

  getCollection(filters?: any): () => Subscription;
}

export abstract class TableStore<T>
  extends ComponentStore<TableStoreState<T>>
  implements TableStoreMethods<T>
{
  protected constructor() {
    super({
      data: [],
      totalElements: 0,
    });
  }

  /**
   * Method to fetch data
   * In switchMap replace of([]) with your fetch request
   * switchMap(() => this.service.getUsers(filters))
   * @param filters
   * @returns
   */
  getCollection = (filters: any) =>
    this.effect((effect) =>
      effect.pipe(
        switchMap(() => of([])),
        tap((data: T[]) => this.setData(data)),
      ),
    );

  /**
   * Set data to store
   *
   * @memberof TableStore
   */
  setData = this.updater((_state, data?: T[]) => ({
    ..._state,
    data,
    size: data.length,
  }));

  setTotalElements = this.updater((_state, totalElements: number) => ({
    ..._state,
    totalElements: totalElements,
  }));

  /**
   * Observable data
   *
   * @memberof TableStore
   */
  readonly data$ = this.select((_state) => _state.data);
  /**
   * Observable totalElements
   *
   * @memberof TableStore
   */
  readonly totalElements$ = this.select((_state) => _state.totalElements);
}
