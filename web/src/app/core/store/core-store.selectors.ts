import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ICoreStore } from './core-store.model';

export const coreStoreSelectors = createFeatureSelector<ICoreStore>('core');

export const selectedLanguage = createSelector(coreStoreSelectors, (_state) => {
  return _state.lang;
});

export const availablePermissions = createSelector(
  coreStoreSelectors,
  (_state) => {
    return _state.permissions;
  }
);

export const isAuthorized = createSelector(
  coreStoreSelectors,
  (_state) => _state.logged
);

export const getWorkshops = createSelector(coreStoreSelectors, (_state) =>
  _state.workshops.map((w) => ({ ...w, accesses: undefined }))
);

  export const getUsers = createSelector(
  coreStoreSelectors,
  (_state) => _state.selectedWorkshop?.accesses
);

export const getUsersByWorkshop = (workshopId: string) =>
  createSelector(coreStoreSelectors, (_state) =>
    _state.workshops.find((w) => w.workshopId === workshopId)
  );
