import { Language } from 'src/app/shared/enums/languages';
import { ICoreStore } from './core-store.model';
import { createReducer, on } from '@ngrx/store';
import { coreStoreActions } from './core-store.actions';
import { Panel } from '@app/shared/enums/panels';

export const initialState: ICoreStore = {
  lang: Language.PL,
  permissions: [Panel.MANAGE_STAFF],
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
  })
);
