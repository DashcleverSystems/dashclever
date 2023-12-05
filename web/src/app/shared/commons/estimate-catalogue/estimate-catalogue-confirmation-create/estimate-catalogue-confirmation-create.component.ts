import { ChangeDetectionStrategy, Component, Self } from '@angular/core';
import { EstimateDto } from 'generated/openapi';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { TranslateModule } from '@ngx-translate/core';
import { EstimateCatalogueService } from '../estimate-catalogue.service';
import { AppSpinner } from '../../spinner/spinner.component';
import { ButtonModule } from 'primeng/button';

@Component({
  templateUrl: './estimate-catalogue-confirmation-create.component.html',
  styleUrl: './estimate-catalogue-confirmation-create.component.scss',
  standalone: true,
  imports: [TranslateModule, AppSpinner, ButtonModule],
  providers: [EstimateCatalogueService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EstimateCatalogueConfirmationCreateComponent {
  estimate: EstimateDto;

  isLoading = false;

  constructor(
    @Self() private service: EstimateCatalogueService,
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
      .createSelectedEstimate(this.estimate.estimateId)
      .subscribe(() => this.ref.close(true));
  }
}
