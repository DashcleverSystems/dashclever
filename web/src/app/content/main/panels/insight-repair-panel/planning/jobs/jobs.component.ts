import { Component, inject } from '@angular/core';
import PlanningStore from '@content/main/panels/insight-repair-panel/planning/planning.store';

@Component({
  selector: 'app-planning-jobs',
  templateUrl: './jobs.component.html',
  styleUrl: './jobs.component.scss',
})
export class JobsComponent {
  private planningStore = inject(PlanningStore);
  labourJobs = this.planningStore.labourJobs;
  paintingJobs = this.planningStore.paintingJobs;
}
