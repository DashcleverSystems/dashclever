import { TableStore } from './table.store';
import { PaginatorState } from 'primeng/paginator';
import { SortDirection } from '@shared/enums/sort-direction';

/**
 * Abstract class for table component
 * To make it work correctly table component need to inject TableStore class
 * @export
 * @abstract
 * @class Table
 * @template T - Interface for type of data
 * @template O - Interface for filters
 */
export abstract class Table<T, O = object> {
  /**
   * Page size
   *
   * @type {number} - default 10
   * @memberof Table
   */
  size: number = 10;
  /**
   * Page page number
   *
   * @type {number} - default 0
   * @memberof Table
   */
  page: number = 0;
  /**
   * Sort direction
   *
   * @type {EstimateFilters.SortDirectionEnum} - default DESC
   * @memberof Table
   */
  sortDirection: SortDirection = SortDirection.DESC;
  /**
   *  For pagination item array with rows per page
   *
   * @type {number[]} - default [5, 10, 15]
   * @memberof Table
   */
  rowsPerPage: number[] = [5, 10, 15];

  /**
   * Last filters
   *
   * @protected
   * @type {Partial<O>}
   * @memberof Table
   */
  protected filters: Partial<O> = {};

  /**
   * Data from store
   *
   * @readonly
   * @memberof Table
   */
  get data$() {
    return this.tableAbstractStore.data$;
  }

  get totalElements$() {
    return this.tableAbstractStore.totalElements$;
  }

  constructor(private tableAbstractStore: TableStore<T>) {}

  /**
   * Method to filter data
   * @param filters
   */
  onFilter(filters: any): void {
    this.filters = filters;
    this.getCollection();
  }
  /**
   * Method to fetch data
   *
   * @memberof Table
   */
  getCollection(): void {
    this.tableAbstractStore.getCollection({
      ...this.filters,
      pageNo: this.page,
      pageSize: this.size,
      sortDirection: this.sortDirection,
    })();
  }

  protected primePageChange(event: PaginatorState): void {
    this.page = event.page;
    this.size = event.rows;
    this.getCollection();
  }
}
