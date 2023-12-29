import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { AccountRestApiService } from '@api/services/accountRestApi.service';

interface AuthState {
  isAuthenticated: boolean;
}

@Injectable()
export class AuthStore extends ComponentStore<AuthState> {
  constructor(private accountApi: AccountRestApiService) {
    super({ isAuthenticated: false });
    this.accountApi.currentUser().subscribe({
      next: () => this.authenticated(true),
      error: () => this.authenticated(false),
    });
  }

  readonly authenticated = this.updater((state, isAuthenticated: boolean) => {
    return {
      ...state,
      isAuthenticated: isAuthenticated,
    };
  });

  readonly isAuthenticated$ = this.select((state) => state.isAuthenticated);
}
