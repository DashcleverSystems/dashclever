import { Component, SkipSelf } from '@angular/core';
import { JobsStore } from '../jobs-store.service';

@Component({
  selector: 'app-repair-process-jobs',
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

  constructor(@SkipSelf() private store: JobsStore) {}
}
