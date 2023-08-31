import { Language } from 'src/app/shared/enums/languages';
import { ICoreStore } from './core-store.model';
import { createReducer, on } from '@ngrx/store';
import { coreStoreActions } from './core-store.actions';

export const initialState: ICoreStore = {
  lang: Language.PL,
};

export const coreStoreReducer = createReducer(
  initialState,
  on(coreStoreActions.changeLanguage, (_state, { lang }) => {
    return {
      ..._state,
      lang: lang,
    };
  })
);
