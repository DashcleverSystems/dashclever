import { Component, Self } from '@angular/core';
import { EstimateDto } from '@api/models/estimateDto';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import {CreatePlanService} from "@content/main/panels/insight-repair-panel/plans/create-confirmation-dialog/create-plan.service";

@Component({
  templateUrl: './create-plan.component.html',
  styleUrl: './create-plan.component.scss',
  providers: [CreatePlanService],
})
export class CreatePlanConfirmationDialog {
  estimate: EstimateDto;

  isLoading = false;

  constructor(
    @Self() private service: CreatePlanService,
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
