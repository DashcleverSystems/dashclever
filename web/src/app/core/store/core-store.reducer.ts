import { Language } from 'src/app/shared/enums/languages';
import { ICoreStore } from './core-store.model';
import { createReducer, on } from '@ngrx/store';
import { coreStoreActions } from './core-store.actions';

export const initialState: ICoreStore = {
  lang: Language.PL,
  mobile: false,
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

  on(coreStoreActions.clearCoreState, (_state) => ({
    ...initialState,
  })),
);
