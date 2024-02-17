import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EmployeeDto, EmployeeOccupationDto, JobDto } from 'generated/openapi';
import { Observable } from 'rxjs';

import moment from 'moment';

@Injectable()
export class InsightRepairAssignService {
  constructor(private http: HttpClient) {}

  getPlanJobsById(id: string): Observable<JobDto[]> {
    return this.http.get<JobDto[]>(`/api/planning/${id}/job`);
  }

  getAllWorkers(): Observable<EmployeeDto[]> {
    return this.http.get<EmployeeDto[]>(`/api/employee`);
  }

  getWorkersOccupationByDay(day: Date): Observable<EmployeeOccupationDto[]> {
    return this.http.get<EmployeeOccupationDto[]>(`/api/employee/occupation`, {
      params: new HttpParams().set('at', moment(day).format('YYYY-MM-DD')),
    });
  }

  assignJob(
    planId: string,
    at: Date,
    workerId: string,
    jobId: number,
  ): Observable<JobDto[]> {
    return this.http.patch<JobDto[]>(`/api/planning/${planId}/job`, [
      {
        employeeId: workerId,
        catalogueJobId: jobId,
        at: moment(at).format('YYYY-MM-DD'),
      },
    ]);
  }

  removeAssigned(planId: string, jobId: number): Observable<any> {
    return this.http.delete(`/api/planning/${planId}/job/${jobId}`);
  }
}
