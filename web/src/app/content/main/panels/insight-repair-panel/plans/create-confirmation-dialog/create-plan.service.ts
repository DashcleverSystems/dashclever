import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import {PlanCreatedNotifier} from "@content/main/panels/insight-repair-panel/plans/create-confirmation-dialog/plan-created-notifier.service";

@Injectable()
export class CreatePlanService {
  constructor(
    private readonly http: HttpClient,
    private readonly planningCreatedNotifier: PlanCreatedNotifier,
  ) {}

  createPlan(estimateId: string): Observable<any> {
    return this.http
      .post('api/planning', undefined, {
        params: new HttpParams().set('estimateId', estimateId),
      })
      .pipe(tap(() => this.planningCreatedNotifier.planningCreated$.next()));
  }
}
