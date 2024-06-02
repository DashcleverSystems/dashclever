import { Component, SkipSelf } from '@angular/core';
import {
  Worker,
  InsightRepairAssignStore,
} from '@content/main/panels/insight-repair-panel/insight-repair-assign/insight-repair-assign.store';
import { Observable } from 'rxjs';
@Component({
  selector: 'app-repair-process-people',
  templateUrl: './people.component.html',
  styleUrl: './people.component.scss',
})
export class InsightRepairPeopleComponent {
  readonly workers$: Observable<Worker[]> = this.store.workers$;
  constructor(@SkipSelf() private store: InsightRepairAssignStore) {}
}
