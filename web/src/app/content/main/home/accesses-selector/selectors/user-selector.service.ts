import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ISelectUser {
  workshopId: string | null;
  employeeId: string | null;
}

@Injectable()
export class UserSelectorService {
  constructor(private http: HttpClient) {}

  selectUser(data: ISelectUser): Observable<any> {
    return this.http.post('/api/account/access', data);
  }
}
