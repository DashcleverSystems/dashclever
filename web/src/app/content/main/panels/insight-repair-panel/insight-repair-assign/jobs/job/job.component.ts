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
import { Translator } from '@angular/compiler-cli/linker/src/file_linker/translator';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-repair-assign-job',
  template: ` <div
    cdkDrag
    cdkDragHandle
    class="job"
    [class.assigned]="isAssignedToSomeone"
    [pTooltip]="tooltipText"
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
      background-color: var(--primary-color);
      color: var(--primary-color-text);
      width: max-content;

      @include shadow();

      &.assigned {
        background-color: var(--surface-500) !important;

        .description {
          color: var(--surface-100);
        }
      }

      .description {
        max-width: calc(30svw - 80px);
        word-break: break-word;
        text-align: center;
        display: flex;
        color: var(--primary-color-text);
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

  get tooltipText(): string {
    let result: string;
    const manHourTranslation =
      this.translateService.instant('general.manHours');
    const hours = ~~(this.job.manMinutes / 60);
    const minutes = this.job.manMinutes % 60;
    result = `${manHourTranslation}: ${hours}:${minutes}`;
    if (this.isAssignedToSomeone) {
      result = `${result}\n${this.worker.name}\n${this.job.assignedAt}`;
    }
    return result;
  }
  constructor(
    @SkipSelf() private store: InsightRepairAssignStore,
    @SkipSelf() private translateService: TranslateService,
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
