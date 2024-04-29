import { Language } from '@app/shared/enums/languages';
import { ICoreStore } from './core-store.model';
import { inject, InjectionToken } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';

const initialState: ICoreStore = {
  lang: Language.PL,
  mobile: false,
};

const CORE_STORE_STATE = new InjectionToken<ICoreStore>('CoreState', {
  factory: () => initialState,
});

const CoreStore = signalStore(
  { providedIn: 'root' },
  withState<ICoreStore>(() => inject(CORE_STORE_STATE)),
  withMethods((store) => ({
    changeLanguage(lang: Language) {
      patchState(store, { lang });
    },
    changeMobile(mobile: boolean) {
      patchState(store, { mobile });
    },
  })),
);

export default CoreStore;
