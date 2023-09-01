import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ICredentials } from '@app/shared/models/user';
import { of } from 'rxjs';

export interface ILoginForm {
  username: FormControl<string | null>;
  password: FormControl<string | null>;
}

@Injectable()
export class AuthService {
  constructor(private fb: FormBuilder, private http: HttpClient) {}

  createForm(): FormGroup<ILoginForm> {
    return this.fb.group({
      username: this.fb.control<string | null>(null, Validators.required),
      password: this.fb.control<string | null>(null, Validators.required),
    });
  }

  login(data: ICredentials) {
    return this.http.post<ICredentials>('/api/login', data);
  }
}
