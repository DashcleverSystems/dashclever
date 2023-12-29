import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ICoreStore } from './core-store.model';

export const coreStoreSelectors = createFeatureSelector<ICoreStore>('core');

export const selectedLanguage = createSelector(coreStoreSelectors, (_state) => {
  return _state.lang;
});

export const getWorkshops = createSelector(coreStoreSelectors, (_state) =>
  _state.workshops.map((w) => ({ ...w, accesses: undefined })),
);

export const getUsers = createSelector(
  coreStoreSelectors,
  (_state) => _state.selectedWorkshop?.accesses,
);

export const getSelectedAccess = createSelector(
  coreStoreSelectors,
  (_state) => _state.selectedAccess,
);
export const getSelectedWorkshop = createSelector(
  coreStoreSelectors,
  (_state) => _state.selectedWorkshop,
);

export const isMobile = createSelector(
  coreStoreSelectors,
  (_state) => _state.mobile,
);
