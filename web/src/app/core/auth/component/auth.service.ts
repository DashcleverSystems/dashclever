import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ICredentials } from '@app/shared/models/user';
import { IWorkshop } from '@app/shared/models/workshop';

export interface ILoginForm {
  username: FormControl<string | null>;
  password: FormControl<string | null>;
}

export type IRegisterForm = {
  email: FormControl<string | null>;
} & ILoginForm;

@Injectable()
export class AuthService {
  constructor(private fb: FormBuilder, private http: HttpClient) {}

  createForm(): FormGroup<ILoginForm> {
    return this.fb.group({
      username: this.fb.control<string | null>(null, Validators.required),
      password: this.fb.control<string | null>(null, Validators.required),
    });
  }

  createRegisterForm(): FormGroup<IRegisterForm> {
    return this.fb.group({
      username: this.fb.control<string | null>(null, Validators.required),
      password: this.fb.control<string | null>(null, Validators.required),
      email: this.fb.control<string | null>(null, [
        Validators.required,
        Validators.email,
      ]),
    });
  }

  login(data: ICredentials) {
    const form = new FormData();
    form.append('username', data.username);
    form.append('password', data.password);

    return this.http.post<void>('/api/login', form);
  }

  logout() {
    return this.http.post<void>('/api/logout', {});
  }

  getPermissions() {
    return this.http.get<IWorkshop[]>('/api/account/access');
  }
}
