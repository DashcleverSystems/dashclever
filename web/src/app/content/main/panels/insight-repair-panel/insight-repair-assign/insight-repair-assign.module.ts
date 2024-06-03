import { NgModule } from '@angular/core';
import { InsightRepairAssign } from './insight-repair-assign.component';
import { JobsComponent } from './jobs/jobs.component';
import { JobComponent } from './jobs/job/job.component';
import { JobsStore } from './jobs-store.service';
import { InsightRepairAssignService } from './insight-repair-assign.service';
import { SharedModule } from '@app/shared/shared.module';
import { WorkersComponent } from '@content/main/panels/insight-repair-panel/insight-repair-assign/workers/workers.component';
import { BubbleComponent } from '@content/main/panels/insight-repair-panel/insight-repair-assign/workers/bubble/bubble.component';
import { DragDropModule } from '@angular/cdk/drag-drop';

@NgModule({
  declarations: [
    InsightRepairAssign,
    JobsComponent,
    JobComponent,
    WorkersComponent,
    BubbleComponent,
  ],
  exports: [],
  imports: [SharedModule, DragDropModule],
  providers: [SharedModule, JobsStore, InsightRepairAssignService],
})
export class InsightRepairAssignModule {}
