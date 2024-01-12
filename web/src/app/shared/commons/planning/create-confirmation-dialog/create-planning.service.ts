import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PlanningCreatedNotifier } from '@shared/commons/planning/create-confirmation-dialog/planning-created.notifier';

@Injectable()
export class CreatePlanningService {
  constructor(
    private readonly http: HttpClient,
    private readonly planningCreatedNotifier: PlanningCreatedNotifier,
  ) {}

  createPlan(estimateId: string): Observable<any> {
    return this.http
      .post('api/planning', undefined, {
        params: new HttpParams().set('estimateId', estimateId),
      })
      .pipe(tap(() => this.planningCreatedNotifier.planningCreated$.next()));
  }
}
