import { Component, Self } from '@angular/core';
import { EstimateDto } from '@api/models/estimateDto';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { CreatePlanningService } from '@app/content/main/panels/insight-repair-panel/planning/create-confirmation-dialog/create-planning.service';

@Component({
  templateUrl: './create-planning.component.html',
  styleUrl: './create-planning.component.scss',
  providers: [CreatePlanningService],
})
export class CreatePlanningConfirmationDialog {
  estimate: EstimateDto;

  isLoading = false;

  constructor(
    @Self() private service: CreatePlanningService,
    public ref: DynamicDialogRef,
    private conf: DynamicDialogConfig,
  ) {
    if (this.conf.data) {
      this.estimate = this.conf.data.estimate;
    }
  }

  onConfirm(): void {
    this.isLoading = true;
    this.service
      .createPlan(this.estimate.id)
      .subscribe(() => this.ref.close(true));
  }
}
