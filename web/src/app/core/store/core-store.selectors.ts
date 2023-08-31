import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ICoreStore } from './core-store.model';

export const coreStoreSelectors = createFeatureSelector<ICoreStore>('core');

export const selectedLanguage = createSelector(coreStoreSelectors, (_state) => {
  return _state.lang;
});
