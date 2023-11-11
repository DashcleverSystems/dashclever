export interface Pageable<T> {
  content: T[];
  totalElements: number;
}

export interface PageableParams {
  number: number;
  size: number;
}
