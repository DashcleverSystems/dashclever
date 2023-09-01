import { Component } from '@angular/core';
import { AuthService, ILoginForm } from './auth.service';
import { FormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { coreStoreActions } from '@app/core/store/core-store.actions';

@Component({
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
})
export class AuthComponent {
  form: FormGroup<ILoginForm> = this.authService.createForm();

  constructor(private authService: AuthService, private store: Store) {}

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }

    const credentials = {
      username: this.form.controls.username.value ?? '',
      password: this.form.controls.password.value ?? '',
    };

    this.store.dispatch(coreStoreActions.login({ credentials }));
  }
}
