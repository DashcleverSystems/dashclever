import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EstimateCreateNotifier {
  // WTF is that ^
  readonly estimateCreated$ = new Subject<void>();
}
