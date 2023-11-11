export abstract class Table<T> {
  size: number = 0;
  page: number = 0;
  totalElements: number = 0;

  rowsPerPage: number[] = [5, 10, 15];
}
