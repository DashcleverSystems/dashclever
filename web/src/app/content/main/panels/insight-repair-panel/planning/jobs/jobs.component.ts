import { Component, SkipSelf } from '@angular/core';
import { PlanningStore } from '../planning.store';

@Component({
  selector: 'app-planning-jobs',
  templateUrl: './jobs.component.html',
  styleUrl: './jobs.component.scss',
})
export class JobsComponent {
  get labourJobs$() {
    return this.store.labouringJobs$;
  }

  get paintingJobs$() {
    return this.store.paintingJobs$;
  }

  constructor(@SkipSelf() private store: PlanningStore) {}
}
