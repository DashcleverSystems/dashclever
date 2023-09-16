export interface DictionaryDTO<
  T = string | number | boolean,
  U = string | number | boolean
> {
  id: T;
  value: U;

  [key: string]: any;
}

export const enumToDictionary = <
  T = string | number | boolean,
  U = string | number | boolean
>(
  data: any,
  nlsCodePrefix?: string,
  customFilters?: (value: string) => boolean,
  isBoolean = false
): DictionaryDTO<T, U>[] => {
  const mapToDictionaries = (value: T) => ({
    id: value as T,
    value: (nlsCodePrefix ? `${nlsCodePrefix}.${value}` : value) as any as U,
  });

  let dictionaryData = Array.isArray(data) ? data : Object.values(data);
  if (isBoolean) {
    dictionaryData = dictionaryData.map((value) =>
      value === 'false' ? false : true
    );
  }

  if (customFilters) {
    return dictionaryData.filter(customFilters).map(mapToDictionaries);
  }

  return dictionaryData.map(mapToDictionaries);
};
