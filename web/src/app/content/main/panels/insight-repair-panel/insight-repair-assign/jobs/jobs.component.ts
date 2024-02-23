import { Component, SkipSelf } from '@angular/core';
import { InsightRepairAssignStore } from '../insight-repair-assign.store';

@Component({
  selector: 'app-repair-process-jobs',
  templateUrl: './jobs.component.html',
  styleUrl: './jobs.component.scss',
})
export class InsightRepairAssignJobs {
  get labourJobs$() {
    return this.store.labouringJobs$;
  }

  get paintingJobs$() {
    return this.store.paintingJobs$;
  }

  constructor(@SkipSelf() private store: InsightRepairAssignStore) {}
}
