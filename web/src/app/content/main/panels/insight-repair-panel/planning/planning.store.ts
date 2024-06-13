import { computed, inject, InjectionToken } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { PlanningService } from '@content/main/panels/insight-repair-panel/planning/planning.service';
import { EmployeeDto, EmployeeOccupationDto, JobDto } from 'generated/openapi';
import { JobType } from '@app/shared/enums/job-type';
import { Workplace } from '@shared/models/employee';
import {
  patchState,
  signalStore,
  withComputed,
  withMethods,
  withState,
} from '@ngrx/signals';

export type Worker = {
  id: string;
  name: string;
  occupation: number;
  assignedJobs: JobDto[];
};

export interface PlanningState {
  jobs: JobDto[];
  employees: EmployeeDto[];
  currentDayOccupation: EmployeeOccupationDto[];
  planId: string | null;
}

const initialState: PlanningState = {
  jobs: [],
  employees: [],
  currentDayOccupation: [],
  planId: null,
};

const PLANNING_STORE_STATE = new InjectionToken<PlanningState>(
  'PlanningState',
  {
    factory: () => initialState,
  },
);

const PlanningStore = signalStore(
  withState<PlanningState>(() => inject(PLANNING_STORE_STATE)),
  withComputed((store) => ({
    paintingJobs: computed<JobDto[]>(() =>
      store.jobs().filter((job) => job.jobType === JobType.VARNISHING),
    ),
    labourJobs: computed<JobDto[]>(() =>
      store.jobs().filter((job) => job.jobType === JobType.LABOUR),
    ),
    workers: computed<Worker[]>(() => {
      const workers = store.employees();
      return workers.map((worker) => {
        const assignedJobs = store
          .jobs()
          .filter((job) => job.assignedTo === worker.id);
        const occupation = store
          .currentDayOccupation()
          .find(
            (occupation) => occupation.employeeId === worker.id,
          )?.manMinutes;
        return {
          id: worker.id,
          name: `${worker.firstName} ${worker.lastName}`,
          occupation: occupation || 0,
          assignedJobs,
        };
      });
    }),
  })),
  withMethods(
    (store, planningService: PlanningService = inject(PlanningService)) => ({
      async removeJobAssignment(jobId: number) {
        const planId = store.planId();
        await firstValueFrom(planningService.removeAssigned(planId, jobId));
        const jobs = await firstValueFrom(
          planningService.getPlanJobsById(planId),
        );
        patchState(store, { jobs });
      },

      setPlanId(planId: string) {
        patchState(store, { planId });
      },

      setJobs(jobs: JobDto[]) {
        patchState(store, { jobs });
      },

      async fetchState() {
        const planId = store.planId();
        const jobs = await firstValueFrom(
          planningService.getPlanJobsById(planId),
        );
        const employees = await firstValueFrom(
          planningService.filterEmployees(Workplace.LABOUR),
        );
        const currentDayOccupation = await firstValueFrom(
          planningService.getEmployeeOccupations(planId, new Date()),
        );
        patchState(store, { jobs, employees, currentDayOccupation, planId });
      },

      async changeDay(day: Date) {
        const currentDayOccupation = await firstValueFrom(
          planningService.getEmployeeOccupations(store.planId(), day),
        );

        patchState(store, { currentDayOccupation });
      },
    }),
  ),
);

export default PlanningStore;
