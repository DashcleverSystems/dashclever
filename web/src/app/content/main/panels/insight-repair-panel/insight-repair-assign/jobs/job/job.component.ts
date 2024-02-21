import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
  SkipSelf,
} from '@angular/core';
import { JobDto } from 'generated/openapi';
import {
  BubbleWorker,
  InsightRepairAssignStore,
} from '@content/main/panels/insight-repair-panel/insight-repair-assign/insight-repair-assign.store';
import { InsightRepairAssignService } from '@content/main/panels/insight-repair-panel/insight-repair-assign/insight-repair-assign.service';

@Component({
  selector: 'app-repair-assign-job',
  template: ` <div
    cdkDrag
    cdkDragHandle
    class="job"
    [class.assigned]="isAssignedToSomeone"
    [pTooltip]="isAssignedTo"
    [tooltipDisabled]="!isAssignedToSomeone"
    [attr.inputId]="job.catalogueJobId"
  >
    <div class="description">
      <span> {{ job.jobDescription }}</span>
      @if (isAssignedToSomeone) {
        <span class="delete" (click)="onRemoveAssignment()"
          ><i class="pi pi-times"></i
        ></span>
      }
    </div>
  </div>`,
  styles: `
    @import 'src/assets/styles/variables.scss';
    @import 'src/assets/styles/mixins.scss';

    .job {
      padding: 10px;
      border-radius: 25px;
      cursor: pointer;
      background-color: var(--input-bg);
      width: max-content;

      @include shadow();

      &.assigned {
        background-color: var(--selected-color) !important;

        .description {
          color: var(--job-list-color-selected);
        }
      }

      .description {
        max-width: calc(30svw - 80px);
        word-break: break-word;
        text-align: center;
        display: flex;
        color: var(--job-list-color);
      }

      .delete {
        color: var(--invalid-color);
        margin-left: 10px;
        display: flex;
        align-items: center;
        justify-content: flex-end;
      }
    }
  `,
})
export class InsightRepairAssignJob implements OnInit, OnChanges {
  @Input({ required: true }) job: JobDto;

  worker?: BubbleWorker;

  get isAssignedToSomeone() {
    return this.job.assignedTo !== null;
  }

  get isAssignedTo() {
    if (this.worker) {
      return `${this.worker.name}\n${this.job.assignedAt}`;
    } else return '';
  }
  constructor(
    @SkipSelf() private store: InsightRepairAssignStore,
    @SkipSelf() private service: InsightRepairAssignService,
  ) {}

  ngOnInit() {
    this.getAssignedWorker();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.job && !changes.job.firstChange) {
      this.getAssignedWorker();
    }
  }

  onRemoveAssignment() {
    this.store.removeJobAssigment(this.job.catalogueJobId);
  }

  private getAssignedWorker() {
    if (this.isAssignedToSomeone) {
      this.store.getWorkerById(this.job.assignedTo).subscribe((worker) => {
        this.worker = worker;
      });
    }
  }
}
