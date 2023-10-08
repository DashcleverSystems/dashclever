import { Component, HostListener, OnInit } from '@angular/core';
import { AuthService, ILoginForm } from './auth.service';
import { FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { coreStoreActions } from '@app/core/store/core-store.actions';
import { isAuthorized } from '@app/core/store/core-store.selectors';
import { EMPTY, catchError, switchMap, take, tap } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
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

  constructor(
    private authService: AuthService,
    private store: Store,
    private router: Router
  ) {
    this.onLogin();
  }

  ngOnInit(): void {
    this.store
      .select(isAuthorized)
      .pipe(
        take(1),
        switchMap((isAuth) => {
          if (!isAuth)
            return this.authService.isLogged().pipe(
              catchError((err) => EMPTY),
              switchMap((isLogged) => this.authService.getPermissions()),
              tap((workshops) => {
                this.store.dispatch(
                  coreStoreActions.loginSuccessfully({ logged: true })
                );
                this.store.dispatch(
                  coreStoreActions.changeWorkshops({ workshops })
                );

                this.router.navigate(['home']);
              })
            );
          else return EMPTY;
        })
      )
      .subscribe();
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
      this.onLogin();
    });
  }
}
