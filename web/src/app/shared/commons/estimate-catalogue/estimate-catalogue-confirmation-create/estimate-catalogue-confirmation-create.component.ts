import { ChangeDetectionStrategy, Component, Self } from '@angular/core';
import { EstimateDto } from 'generated/openapi';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { TranslateModule } from '@ngx-translate/core';
import { AppSpinner } from '../../spinner/spinner.component';
import { ButtonModule } from 'primeng/button';
import { EstimateCatalogueConfirmationCreateService } from '@shared/commons/estimate-catalogue/estimate-catalogue-confirmation-create/estimate-catalogue-confirmation-create.service';

@Component({
  templateUrl: './estimate-catalogue-confirmation-create.component.html',
  styleUrl: './estimate-catalogue-confirmation-create.component.scss',
  standalone: true,
  imports: [TranslateModule, AppSpinner, ButtonModule],
  providers: [EstimateCatalogueConfirmationCreateService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EstimateCatalogueConfirmationCreateComponent {
  estimate: EstimateDto;

  isLoading = false;

  constructor(
    @Self() private service: EstimateCatalogueConfirmationCreateService,
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
