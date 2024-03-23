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
    const filterMetadata = this.getFilterMetadata(
      this.primeFilters,
      primeFilterKey,
    );
    const filterValue = primeFilterValueGetter(filterMetadata);
    filterSetter(this.filters, filterValue);
  }

  private getFilterMetadata(
    primeFilters: {
      [s: string]: FilterMetadata | FilterMetadata[] | undefined;
    },
    primeFilterKey: string,
  ): FilterMetadata | FilterMetadata[] | null {
    for (const key in primeFilters) {
      if (key === primeFilterKey) return primeFilters[key];
    }
    return null;
  }
}
