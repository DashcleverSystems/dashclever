import { FilterMetadata } from 'primeng/api/filtermetadata';

export type PrimeFilters = {
  [s: string]: FilterMetadata | FilterMetadata[] | undefined;
};

export class FiltersSetter<T> {
  constructor(
    private filters: T,
    private primeFilters: PrimeFilters,
  ) {}

  public setFilter<K>(
    filterSetter: (filters: T, primeFilterValue: K) => void,
    primeFilterKey: string,
    primeFilterValueGetter: (
      filterMetadata: FilterMetadata | FilterMetadata[] | null,
    ) => K | null = (filterMetadata) => {
      if (!filterMetadata) return null;
      return filterMetadata[0]?.value ?? null;
    },
  ) {
    const filterMetadata = this.primeFilters[primeFilterKey] ?? null;
    const filterValue = primeFilterValueGetter(filterMetadata);
    filterSetter(this.filters, filterValue);
  }
}
