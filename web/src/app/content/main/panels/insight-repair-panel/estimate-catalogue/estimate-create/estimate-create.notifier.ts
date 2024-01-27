import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EstimateCreateNotifier {
  readonly estimateCreated$ = new Subject<void>();
}
