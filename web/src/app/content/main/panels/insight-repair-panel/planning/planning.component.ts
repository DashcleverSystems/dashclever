import { Component, ElementRef, inject, OnInit, SkipSelf } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { catchError, distinctUntilChanged, EMPTY, switchMap } from 'rxjs';
import { isEqual } from 'lodash';
import { PlanningService } from '@content/main/panels/insight-repair-panel/planning/planning.service';
import { RepairingApiService } from '@api/services/repairingApi.service';
import { IToastMessage, ToastService } from '@shared/services/toast.service';
import { PlanningService } from '@content/main/panels/insight-repair-panel/planning/planning.service';
import PlanningStore from '@content/main/panels/insight-repair-panel/planning/planning.store';

@Component({
  selector: 'app-insight-repair-assign',
  templateUrl: './planning.component.html',
  styleUrl: './planning.component.scss',
  providers: [PlanningStore],
})
export class PlanningComponent implements OnInit {
  private planningStore = inject(PlanningStore);

  selectedDate: FormControl<Date> = new FormControl({
    value: new Date(),
    disabled: false,
  });

  readonly planId: string;

  constructor(
    @SkipSelf() private route: ActivatedRoute,
    @SkipSelf() private service: PlanningService,
    @SkipSelf() private repairingApi: RepairingApiService,
    @SkipSelf() private router: Router,
    @SkipSelf() private toast: ToastService,
  ) {
    this.planId = this.route.snapshot.paramMap.get('id');
    this.planningStore.setPlanId(this.planId);
    this.planningStore.fetchState();
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
            this.planningStore.setJobs(newJobsList);
            this.planningStore.changeDay(date);
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
        this.planningStore.changeDay(date);
      });
  }
}
