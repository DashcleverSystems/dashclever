import { NgModule } from '@angular/core';
import { CommonsModule } from '@shared/commons/commons.module';
import { RepairProcessPanelComponent } from '@content/main/panels/repair-process-panel/repair-process-panel.component';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [RepairProcessPanelComponent],
  imports: [TranslateModule, CommonsModule],
  providers: [],
})
export class ManagePanelModule {}
