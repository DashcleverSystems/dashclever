import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { combineLatest, Observable, of, switchMap, take, tap } from 'rxjs';
import { InsightRepairAssignService } from './insight-repair-assign.service';
import { EmployeeDto, EmployeeOccupationDto, JobDto } from 'generated/openapi';
import { JobType } from '@app/shared/enums/job-type';

export type BubbleWorker = {
  id: string;
  name: string;
  occupation: number;
  assignedJobs: JobDto[];
};
export interface InsightRepairAssignState {
  jobs: JobDto[];
  workers: EmployeeDto[];
  currentDayOccupation: EmployeeOccupationDto[];
}

const initialState: InsightRepairAssignState = {
  jobs: [],
  workers: [],
  currentDayOccupation: [],
};

@Injectable()
export class InsightRepairAssignStore extends ComponentStore<InsightRepairAssignState> {
  readonly loadCollection = this.effect((planningId$: Observable<string>) =>
    planningId$.pipe(
      switchMap((planningId) =>
        combineLatest<[JobDto[], EmployeeDto[], EmployeeOccupationDto[]]>([
          this.service.getPlanJobsById(planningId),
          this.service.getAllWorkers(),
          this.service.getWorkersOccupationByDay(new Date()),
        ]),
      ),
      tap(
        ([jobs, workers, currentDayOccupation]: [
          JobDto[],
          EmployeeDto[],
          EmployeeOccupationDto[],
        ]) => {
          this.setData({ jobs, workers, currentDayOccupation });
        },
      ),
    ),
  );

  readonly setData = this.updater(
    (_state, data: Partial<InsightRepairAssignState>) => ({
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

  readonly workers$: Observable<BubbleWorker[]> = this.select((_state) => {
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

  constructor(private service: InsightRepairAssignService) {
    super(initialState);
  }

  readonly getWorkerById = (id: string) =>
    this.workers$.pipe(
      take(1),
      switchMap((workers) => of(workers.find((worker) => worker.id === id))),
    );
}
