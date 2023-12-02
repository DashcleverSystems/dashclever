import { Language } from 'src/app/shared/enums/languages';
import { ICoreStore } from './core-store.model';
import { createReducer, on } from '@ngrx/store';
import { coreStoreActions } from './core-store.actions';

export const initialState: ICoreStore = {
  lang: Language.PL,
  mobile: false,
  permissions: [],
  logged: false,
  workshops: [],
  selectedWorkshop: undefined,
  selectedAccess: undefined,
};

export const coreStoreReducer = createReducer(
  initialState,

  on(coreStoreActions.changeLanguage, (_state, { lang }) => {
    return {
      ..._state,
      lang,
    };
  }),

  on(coreStoreActions.changeAppView, (_state, { mobile }) => ({
    ..._state,
    mobile,
  })),

  on(coreStoreActions.changePermissions, (_state, { permissions }) => {
    return {
      ..._state,
      permissions,
    };
  }),

  on(coreStoreActions.loginSuccessfully, (_state, { logged }) => {
    return {
      ..._state,
      logged,
    };
  }),

  on(coreStoreActions.loginFail, (_state) => {
    return {
      ..._state,
      permissions: [],
      logged: false,
    };
  }),

  on(coreStoreActions.logoutSuccessfully, (_state) => ({
    ..._state,
    logged: false,
    permissions: [],
    workshops: [],
    selectedWorkshop: undefined,
    selectedAccess: undefined,
  })),

  on(coreStoreActions.changeWorkshops, (_state, { workshops }) => ({
    ..._state,
    workshops: workshops,
  })),

  on(coreStoreActions.selectWorkshop, (_state, { workshop }) => {
    const foundWorkshop = _state.workshops.find(
      (w) => w.workshopId === workshop?.workshopId,
    );

    return {
      ..._state,
      selectedWorkshop: foundWorkshop ?? undefined,
      selectedAccess: undefined,
      permissions: [],
    };
  }),
  on(coreStoreActions.selectWorkshopByWorkshopId, (_state, { workshopId }) => {
    const foundWorkshop = _state.workshops.find(
      (w) => w.workshopId === workshopId,
    );

    return {
      ..._state,
      selectedWorkshop: foundWorkshop ?? undefined,
      selectedAccess: undefined,
      permissions: [],
    };
  }),

  on(coreStoreActions.selectAccess, (_state, { access }) => ({
    ..._state,
    selectedAccess: access,
    permissions: access?.authorities ?? [],
  })),

  on(coreStoreActions.clearCoreState, (_state) => ({
    ...initialState,
  })),
);
