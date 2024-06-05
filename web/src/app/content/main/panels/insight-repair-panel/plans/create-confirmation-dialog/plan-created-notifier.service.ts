import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PlanCreatedNotifier {
  readonly planningCreated$ = new Subject<void>();
}
