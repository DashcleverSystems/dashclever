import { inject } from '@angular/core';
import {
  patchState,
  signalStore,
  withHooks,
  withMethods,
  withState,
} from '@ngrx/signals';
import { AccountRestApiService } from 'generated/openapi';
import { firstValueFrom } from 'rxjs';

interface AuthState {
  isAuthenticated: boolean;
}

const initialState: AuthState = {
  isAuthenticated: false,
};

const AuthStore = signalStore(
  withState<AuthState>(initialState),
  withMethods((store, accountApi = inject(AccountRestApiService)) => ({
    async initStore(): Promise<void> {
      patchState(store, { isAuthenticated: false });

      const isAuth = await firstValueFrom(accountApi.currentUser());

      if (!!isAuth) {
        patchState(store, { isAuthenticated: true });
      }
    },
    changeIsAuth(isAuth: boolean) {
      patchState(store, { isAuthenticated: isAuth });
    },
  })),
  withHooks({
    async onInit(store) {
      await store.initStore();
    },
  }),
);

export default AuthStore;
