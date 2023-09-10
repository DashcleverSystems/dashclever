import { Component } from '@angular/core';
import { AuthService, ILoginForm } from './auth.service';
import { FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { coreStoreActions } from '@app/core/store/core-store.actions';

@Component({
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
})
export class AuthComponent {
  isRegisterForm: boolean = false;

  form: FormGroup<ILoginForm> = this.authService.createForm();

  constructor(private authService: AuthService, private store: Store) {
    this.onLogin();
  }

  get emailControl() {
    const control = this.form.controls['email'];
    if (control) {
      return control && this.isRegisterForm;
    }
    return false;
  }

  onRegister(): void {
    this.isRegisterForm = true;
    this.form.controls.email.setValidators([
      Validators.email,
      Validators.required,
    ]);
    this.form.updateValueAndValidity();
    this.form.reset();
  }

  onLogin(): void {
    this.isRegisterForm = false;
    this.form.controls.email.removeValidators([
      Validators.email,
      Validators.required,
    ]);
    this.form.updateValueAndValidity();

    this.form.reset();
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

  onRegisterSubmit(): void {
    if (this.form.invalid) {
      return;
    }

    const credentials = {
      username: this.form.controls.username.value ?? '',
      password: this.form.controls.password.value ?? '',
      email: this.form.controls.email.value ?? '',
    };

    this.authService.register(credentials).subscribe(() => {
      this.form.reset();
      this.isRegisterForm = false;
    });
  }
}
