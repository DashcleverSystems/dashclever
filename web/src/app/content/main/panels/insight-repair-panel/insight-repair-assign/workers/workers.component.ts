import { Component, SkipSelf } from '@angular/core';
import {
  Worker,
  JobsStore,
} from '@content/main/panels/insight-repair-panel/insight-repair-assign/jobs-store.service';
import { Observable } from 'rxjs';
@Component({
  selector: 'app-planning-workers',
  templateUrl: './workers.component.html',
  styleUrl: './workers.component.scss',
})
export class WorkersComponent {
  readonly workers$: Observable<Worker[]> = this.store.workers$;
  constructor(@SkipSelf() private store: JobsStore) {}
}
