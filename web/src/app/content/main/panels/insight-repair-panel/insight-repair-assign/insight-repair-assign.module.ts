import { NgModule } from '@angular/core';
import { InsightRepairAssign } from './insight-repair-assign.component';
import { InsightRepairAssignJobs } from './jobs/jobs.component';
import { InsightRepairAssignJob } from './jobs/job/job.component';
import { InsightRepairAssignStore } from './insight-repair-assign.store';
import { InsightRepairAssignService } from './insight-repair-assign.service';
import { SharedModule } from '@app/shared/shared.module';
import { InsightRepairPeopleComponent } from './people/people.component';
import { InsightRepairAssignBubble } from './people/bubble/bubble.component';
import { DragDropModule } from '@angular/cdk/drag-drop';

@NgModule({
  declarations: [
    InsightRepairAssign,
    InsightRepairAssignJobs,
    InsightRepairAssignJob,
    InsightRepairPeopleComponent,
    InsightRepairAssignBubble,
  ],
  exports: [],
  imports: [SharedModule, DragDropModule],
  providers: [
    SharedModule,
    InsightRepairAssignStore,
    InsightRepairAssignService,
  ],
})
export class InsightRepairAssignModule {}
