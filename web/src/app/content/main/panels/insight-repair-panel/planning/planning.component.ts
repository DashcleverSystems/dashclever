import { Component, ElementRef, OnInit, SkipSelf } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PlanningStore } from './planning.store';
import { FormControl } from '@angular/forms';
import { catchError, distinctUntilChanged, EMPTY, switchMap } from 'rxjs';
import { isEqual } from 'lodash';
import { PlanningService } from '@content/main/panels/insight-repair-panel/planning/planning.service';
import { RepairingApiService } from '@api/services/repairingApi.service';
import { IToastMessage, ToastService } from '@shared/services/toast.service';

@Component({
  selector: 'app-insight-repair-assign',
  templateUrl: './planning.component.html',
  styleUrl: './planning.component.scss',
})
export class PlanningComponent implements OnInit {
  selectedDate: FormControl<Date> = new FormControl({
    value: new Date(),
    disabled: false,
  });

  readonly planId: string;

  constructor(
    @SkipSelf() private route: ActivatedRoute,
    @SkipSelf() private store: PlanningStore,
    @SkipSelf() private service: PlanningService,
    @SkipSelf() private repairingApi: RepairingApiService,
    @SkipSelf() private router: Router,
    @SkipSelf() private toast: ToastService,
  ) {
    this.planId = this.route.snapshot.paramMap.get('id');

    this.store.loadCollection(this.planId);
  }

  ngOnInit() {
    this.subscribeDateChange();
  }

  drop(event: any) {
    const dropPoints = event.dropPoint;
    const item: ElementRef<HTMLDivElement> = event.item.element;

    if ('x' in dropPoints && 'y' in dropPoints && item) {
      const dropContainer = document.elementFromPoint(
        dropPoints.x,
        dropPoints.y,
      );
      const closestDropContainerBubble =
        dropContainer.closest('.bubble-container');

      if (closestDropContainerBubble) {
        const workerId = closestDropContainerBubble.getAttribute('inputid');
        const date = this.selectedDate.value;
        const jobId = Number(item.nativeElement.getAttribute('inputid'));

        this.service
          .assignJob(this.planId, date, workerId, jobId)
          .pipe(
            catchError(() => {
              return EMPTY;
            }),
          )
          .subscribe((newJobsList) => {
            this.store.setData({ jobs: newJobsList });
            this.store.updateOccupationByDate(date);
          });
      }
    }
  }

  startRepair() {
    const successToastMessage: IToastMessage = {
      message: 'components.planning.start-repair.success',
      translate: true,
    };
    this.repairingApi
      .startRepairOfPlan(this.planId)
      .pipe(
        switchMap(() =>
          this.router.navigate(['/insight-repair/estimate-catalogue']),
        ),
      )
      .subscribe((success) => {
        if (success) {
          this.toast.success(successToastMessage);
        }
      });
  }

  private subscribeDateChange(): void {
    this.selectedDate.valueChanges
      .pipe(distinctUntilChanged(isEqual))
      .subscribe((date) => {
        this.store.updateOccupationByDate(date);
      });
  }
}
