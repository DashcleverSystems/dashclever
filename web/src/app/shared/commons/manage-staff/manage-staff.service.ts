import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEmployee } from '@shared/models/employee';

@Injectable()
export class ManageStaffService {
  constructor(private http: HttpClient) {}

  getEmployees(workshopId: string): Observable<IEmployee[]> {
    return this.http.get<IEmployee[]>(`/api/workshop/${workshopId}/employee`);
  }
}
