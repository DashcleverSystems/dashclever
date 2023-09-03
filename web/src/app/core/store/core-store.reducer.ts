import { Language } from 'src/app/shared/enums/languages';
import { ICoreStore } from './core-store.model';
import { createReducer, on } from '@ngrx/store';
import { coreStoreActions } from './core-store.actions';

export const initialState: ICoreStore = {
  lang: Language.PL,
  permissions: [],
  logged: false,
  workshops: [],
};

export const coreStoreReducer = createReducer(
  initialState,
  on(coreStoreActions.changeLanguage, (_state, { lang }) => {
    return {
      ..._state,
      lang,
    };
  }),
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
  })),
  on(coreStoreActions.changeWorkshops, (_state, { workshops }) => ({
    ..._state,
    workshops: workshops,
  }))
);
