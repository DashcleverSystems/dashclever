import {Component, SkipSelf} from '@angular/core';
import {Observable} from 'rxjs';
import {PlanningStore, Worker} from "@content/main/panels/insight-repair-panel/planning/planning.store";

@Component({
  selector: 'app-planning-workers',
  templateUrl: './workers.component.html',
  styleUrl: './workers.component.scss',
})
export class WorkersComponent {
  readonly workers$: Observable<Worker[]> = this.store.workers$;

  constructor(@SkipSelf() private store: PlanningStore) {
  }
}
