import { Component, inject } from '@angular/core';
import PlanningStore from '@content/main/panels/insight-repair-panel/planning/planning.store';

@Component({
  selector: 'app-planning-workers',
  templateUrl: './workers.component.html',
  styleUrl: './workers.component.scss',
})
export class WorkersComponent {
  private planningStore = inject(PlanningStore);
  workers = this.planningStore.workers;
}
