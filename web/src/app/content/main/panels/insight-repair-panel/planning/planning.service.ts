import { Injectable } from '@angular/core';
import {
  EmployeeApiService,
  EmployeeDto,
  EmployeeOccupationDto,
  JobDto,
} from 'generated/openapi';
import { Observable } from 'rxjs';

import moment from 'moment';
import { Workplace } from '@shared/models/employee';
import { PlanningApiService } from '@api/services/planningApi.service';
import { Assignment } from '@api/models/assignment';

@Injectable()
export class PlanningService {
  constructor(
    private readonly employeeApi: EmployeeApiService,
    private readonly planningApi: PlanningApiService,
  ) {}

  getPlanJobsById(id: string): Observable<JobDto[]> {
    return this.planningApi.getAllByPlanningId(id);
  }

  filterEmployees(workplace: Workplace): Observable<EmployeeDto[]> {
    return this.employeeApi.getAll(workplace);
  }

  getWorkersOccupationByDay(
    planId: string,
    day: Date,
  ): Observable<EmployeeOccupationDto[]> {
    return this.planningApi.getAllEmployeeOccupations(
      planId,
      moment(day).format('YYYY-MM-DD'),
    );
  }

  assignJob(
    planId: string,
    at: Date,
    workerId: string,
    jobId: number,
  ): Observable<JobDto[]> {
    const assignment: Assignment = {
      employeeId: workerId,
      catalogueJobId: jobId,
      at: moment(at).format('YYYY-MM-DD'),
    };
    return this.planningApi.assignJobs(planId, [assignment]);
  }

  removeAssigned(planId: string, jobId: number): Observable<any> {
    return this.planningApi.removeAssignment(planId, jobId);
  }
}
