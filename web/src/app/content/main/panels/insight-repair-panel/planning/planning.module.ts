import { NgModule } from '@angular/core';
import { PlanningComponent } from './planning.component';
import { JobsComponent } from './jobs/jobs.component';
import { PlanningService } from './planning.service';
import { SharedModule } from '@app/shared/shared.module';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { WorkersComponent } from '@content/main/panels/insight-repair-panel/planning/workers/workers.component';
import { BubbleComponent } from '@content/main/panels/insight-repair-panel/planning/workers/bubble/bubble.component';
import { JobComponent } from '@content/main/panels/insight-repair-panel/planning/jobs/job/job.component';

@NgModule({
  declarations: [
    BubbleComponent,
    WorkersComponent,
    JobComponent,
    JobsComponent,
    PlanningComponent,
  ],
  exports: [],
  imports: [SharedModule, DragDropModule],
  providers: [SharedModule, PlanningService],
})
export class PlanningModule {}
