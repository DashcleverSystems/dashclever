import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ICredentials } from '@app/shared/models/user';
import { AccountRestApiService } from '@api/services/accountRestApi.service';

export interface ILoginForm {
  username: FormControl<string | null>;
  password: FormControl<string | null>;
  email: FormControl<string | null>;
}

@Injectable()
export class AuthService {
  constructor(
    private fb: FormBuilder,
    private accountApi: AccountRestApiService,
    private http: HttpClient,
  ) {}

  createForm(): FormGroup<ILoginForm> {
    const form = this.fb.group({
      username: this.fb.control<string | null>(null, [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(24),
      ]),
      password: this.fb.control<string | null>(null, Validators.required),
      email: this.fb.control<string | null>(null, [
        Validators.required,
        Validators.email,
      ]),
    });

    return form;
  }

  login(data: ICredentials) {
    const form = new FormData();
    form.append('username', data.username);
    form.append('password', data.password);

    return this.http.post<void>('api/login', form);
  }

  register(data: ICredentials & { email: string }) {
    const registerReq = {
      username: data.username,
      password: data.password,
      email: data.email,
    };
    return this.accountApi.register(registerReq);
  }
}
