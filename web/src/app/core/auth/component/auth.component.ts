import { Component } from '@angular/core';
import { AuthService, ILoginForm, IRegisterForm } from './auth.service';
import { FormControl, FormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { coreStoreActions } from '@app/core/store/core-store.actions';

@Component({
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
})
export class AuthComponent {
  isRegisterForm: boolean = false;

  form: FormGroup<ILoginForm | IRegisterForm> = this.authService.createForm();

  constructor(private authService: AuthService, private store: Store) {}

  get emailControl() {
    const control = this.form.get('email');
    if (control) {
      return control;
    }
    return undefined;
  }

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
