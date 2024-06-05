import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { combineLatest, Observable, of, switchMap, take, tap } from 'rxjs';
import { PlanningService } from '@content/main/panels/insight-repair-panel/planning/planning.service';
import { EmployeeDto, EmployeeOccupationDto, JobDto } from 'generated/openapi';
import { JobType } from '@app/shared/enums/job-type';
import { Workplace } from '@shared/models/employee';

export type Worker = {
  id: string;
  name: string;
  occupation: number;
  assignedJobs: JobDto[];
};

export interface PlanningState {
  jobs: JobDto[];
  workers: EmployeeDto[];
  currentDayOccupation: EmployeeOccupationDto[];
  planId: string | null;
}

const initialState: PlanningState = {
  jobs: [],
  workers: [],
  currentDayOccupation: [],
  planId: null,
};

@Injectable()
export class PlanningStore extends ComponentStore<PlanningState> {
  readonly loadCollection = this.effect((planningId$: Observable<string>) =>
    planningId$.pipe(
      switchMap((planningId) =>
        combineLatest<
          [JobDto[], EmployeeDto[], EmployeeOccupationDto[], string]
        >([
          this.service.getPlanJobsById(planningId),
          this.service.filterEmployees(Workplace.LABOUR),
          this.service.getWorkersOccupationByDay(new Date()),
          of(planningId),
        ]),
      ),
      tap(
        ([jobs, employees, currentDayOccupation, planId]: [
          JobDto[],
          EmployeeDto[],
          EmployeeOccupationDto[],
          string,
        ]) => {
          this.setData({
            jobs,
            workers: employees,
            currentDayOccupation,
            planId,
          });
        },
      ),
    ),
  );

  readonly setData = this.updater(
    (_state, data: Partial<PlanningState>) => ({
      ..._state,
      ...data,
    }),
  );

  readonly updateOccupationByDate = this.effect(($effect: Observable<Date>) =>
    $effect.pipe(
      switchMap((date: Date) => this.service.getWorkersOccupationByDay(date)),
      tap((currentDayOccupation: EmployeeOccupationDto[]) => {
        this.setData({ currentDayOccupation });
      }),
    ),
  );

  readonly paintingJobs$ = this.select((_state) =>
    _state.jobs.filter((job) => job.jobType === JobType.VARNISHING),
  );

  readonly labouringJobs$ = this.select((_state) =>
    _state.jobs.filter((job) => job.jobType === JobType.LABOUR),
  );

  readonly workers$: Observable<Worker[]> = this.select((_state) => {
    return _state.workers.map((worker) => {
      const assignedJobs = _state.jobs.filter(
        (job) => job.assignedTo === worker.id,
      );
      const occupation = _state.currentDayOccupation.find(
        (occupation) => occupation.employeeId === worker.id,
      )?.manMinutes;
      return {
        id: worker.id,
        name: `${worker.firstName} ${worker.lastName}`,
        occupation: occupation || 0,
        assignedJobs,
      };
    });
  });

  readonly removeJobAssigment = this.effect(($effect: Observable<number>) =>
    $effect.pipe(
      switchMap((jobId: number) =>
        this.service.removeAssigned(this.get().planId, jobId),
      ),
      tap(() => this.loadCollection(this.get().planId)),
    ),
  );

  constructor(private service: PlanningService) {
    super(initialState);
  }

  readonly getWorkerById = (id: string) =>
    this.workers$.pipe(
      take(1),
      switchMap((workers) => of(workers.find((worker) => worker.id === id))),
    );
}
