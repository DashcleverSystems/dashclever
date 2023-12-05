import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable()
export class WorkshopCreatedNotifier {
  private workshopCreated$ = new Subject<void>();

  get workshopCreatedListener(): Observable<void> {
    return this.workshopCreated$.asObservable();
  }

  notify() {
    this.workshopCreated$.next();
  }
}
