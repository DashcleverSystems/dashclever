import { Component, effect, HostListener, inject, OnInit } from '@angular/core';
import { AuthService, ILoginForm } from './auth.service';
import { FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastService } from '@shared/services/toast.service';
import AuthStore from './auth.store';

@Component({
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
  providers: [AuthStore],
})
export class AuthComponent implements OnInit {
  @HostListener('keydown', ['$event']) onKeyDown = (e: any) => {
    if (e.keyCode === 13 && e.key === 'Enter') {
      this.onSubmit();
    }
  };

  isRegisterForm: boolean = false;
  form: FormGroup<ILoginForm> = this.authService.createForm();

  get emailControl() {
    const control = this.form.controls['email'];
    if (control) {
      return control && this.isRegisterForm;
    }
    return false;
  }

  private authStore = inject(AuthStore);

  constructor(
    private authService: AuthService,
    private toast: ToastService,
    private router: Router,
  ) {
    effect(() => {
      const isAuth = this.authStore.isAuthenticated();

      if (isAuth) {
        this.router.navigate(['home']);
      }
    });
  }

  ngOnInit(): void {
    this.onLogin();
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

    this.authService.login(credentials).subscribe({
      next: () => this.authStore.changeIsAuth(true),
      error: (response) => {
        this.authStore.changeIsAuth(false);
        if (response.status == 400) {
          this.toast.warn({
            title: 'toast.wrongCredentials.title',
            message: 'toast.wrongCredentials.message',
            translate: true,
          });
        }
      },
    });
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
      this.onLogin();
    });
  }
}
